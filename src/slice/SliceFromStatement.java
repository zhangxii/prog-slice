package slice;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.classLoader.Language;
import com.ibm.wala.classLoader.ShrikeBTMethod;
import com.ibm.wala.ipa.callgraph.*;
import com.ibm.wala.ipa.callgraph.AnalysisOptions.ReflectionOptions;
import com.ibm.wala.ipa.callgraph.impl.AllApplicationEntrypoints;
import com.ibm.wala.ipa.callgraph.impl.Util;
//import com.ibm.wala.ipa.callgraph.impl.Util;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.callgraph.propagation.PointerAnalysis;
import com.ibm.wala.ipa.cha.*;
import com.ibm.wala.ipa.modref.ModRef;
import com.ibm.wala.ipa.slicer.NormalStatement;
import com.ibm.wala.ipa.slicer.SDG;
import com.ibm.wala.ipa.slicer.Slicer;
import com.ibm.wala.ipa.slicer.Slicer.ControlDependenceOptions;
import com.ibm.wala.ipa.slicer.Slicer.DataDependenceOptions;
import com.ibm.wala.ipa.slicer.Statement;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAInvokeInstruction;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.config.AnalysisScopeReader;
import com.ibm.wala.util.io.FileProvider;
import com.ibm.wala.util.warnings.Warnings;

public class SliceFromStatement{
	public static void sliceFromAPI(String jar,String callname, String callmethodClass, String methodName) throws IOException, ClassHierarchyException, IllegalArgumentException, InvalidClassFileException, CancelException {
		//Access  a file
		File exFile=new FileProvider().getFile("Java60RegressionExclusions.txt");
		AnalysisScope scope = AnalysisScopeReader.makeJavaBinaryAnalysisScope(jar, exFile);	
		//Build ClassHierarchy
		ClassHierarchy cha = ClassHierarchyFactory.make(scope);
		
		//Build Entrypoint
//		Iterable<Entrypoint> entrypoints = new AllApplicationEntrypoints(scope, cha);
		Iterable<Entrypoint> entrypoints =Util.makeMainEntrypoints(scope, cha);
		AnalysisOptions options = new AnalysisOptions(scope, entrypoints);
		options.setReflectionOptions(ReflectionOptions.NONE); 
		
		CallGraphBuilder<InstanceKey> builder = Util.makeVanillaZeroOneCFABuilder(Language.JAVA, options, new AnalysisCacheImpl(), cha, scope);
//		CallGraphBuilder<InstanceKey> builder = Util.makeZeroCFABuilder(Language.JAVA, options, new AnalysisCacheImpl(), cha, scope);
		
		System.out.println("building call graph...");
		CallGraph cg = builder.makeCallGraph(options, null);
        System.out.println("done");
        System.out.println(CallGraphStats.getStats(cg));
        
        // Build sdg
		final PointerAnalysis<InstanceKey> pa = builder.getPointerAnalysis();
		ModRef<InstanceKey> modRef = ModRef.make(); 
		SDG<?> sdg = new SDG<>(cg, pa, modRef, DataDependenceOptions.NO_HEAP, ControlDependenceOptions.NO_EXCEPTIONAL_EDGES, null);
		
		CGNode nc = null;
		Collection<Statement> collection = null;

		CGNode n = FindMethod.findMethod(cg, callname, callmethodClass);
		Statement statement = FindStatement.findCallTo(n, methodName);
		
		//backward slice
		System.out.println("-------computeBackwardSlice-------");
		collection = Slicer.computeBackwardSlice(sdg, statement);
		dumpSlice(collection, callname);
		//forward slice
//		System.out.println("-------computeForwardSlice-------");
//		collection = Slicer.computeForwardSlice(sdg, statement);		
//		dumpSlice(collection, callname); 
	}
	
	public static void sliceFromReturn(String jar,String callname, String callmethodClass) throws IOException, ClassHierarchyException, IllegalArgumentException, InvalidClassFileException, CancelException {
		File exFile=new FileProvider().getFile("Java60RegressionExclusions.txt");
		AnalysisScope scope = AnalysisScopeReader.makeJavaBinaryAnalysisScope(jar, exFile);	
		ClassHierarchy cha = ClassHierarchyFactory.make(scope);
		
		Iterable<Entrypoint> entrypoints = new AllApplicationEntrypoints(scope, cha);
		AnalysisOptions options = new AnalysisOptions(scope, entrypoints);
		options.setReflectionOptions(ReflectionOptions.NONE); 
		
		CallGraphBuilder<InstanceKey> builder = Util.makeVanillaZeroOneCFABuilder(Language.JAVA, options, new AnalysisCacheImpl(), cha, scope);
//		CallGraphBuilder<InstanceKey> builder = Util.makeZeroCFABuilder(Language.JAVA, options, new AnalysisCacheImpl(), cha, scope);
		
		System.out.println("building call graph...");
		CallGraph cg = builder.makeCallGraph(options, null);
        System.out.println("done");
        System.out.println(CallGraphStats.getStats(cg));
               
		final PointerAnalysis<InstanceKey> pa = builder.getPointerAnalysis();
		ModRef<InstanceKey> modRef = ModRef.make(); 
		SDG<?> sdg = new SDG<>(cg, pa, modRef, DataDependenceOptions.FULL, ControlDependenceOptions.NO_EXCEPTIONAL_EDGES, null);
		CGNode nc = null;
		Collection<Statement> collection = null;
		CGNode n = FindMethod.findMethod(cg, callname, callmethodClass);
		Statement statement = FindStatement.findReturn(n);
		
		//backward slice
		System.out.println("-------computeBackwardSlice-------");
		collection = Slicer.computeBackwardSlice(sdg, statement);		
		dumpSlice(collection, callname);
		
//		//forward slice
//		System.out.println("-------computeForwardSlice-------");
//		collection = Slicer.computeForwardSlice(sdg, statement);		
//		dumpSlice(collection, callname);               
			 
	}
	
	
	public static void dumpSlice(Collection<Statement> slice,String callname) throws InvalidClassFileException {
	      for (Statement s : slice) {
	    	  if(s.getKind() == Statement.Kind.NORMAL)	
	    		  System.out.println(s);
	    	  CGNode node = s.getNode();
	    	  //come from wala net:
	    	  //http://wala.sourceforge.net/wiki/index.php/UserGuide:MappingToSourceCode#From_Slices_to_source_line_numbers
	    	  if (s.getKind() == Statement.Kind.NORMAL) { 
	    		   int bcIndex, instructionIndex = ((NormalStatement) s).getInstructionIndex();
	    		   IMethod met = s.getNode().getMethod();	        	  
	    			  try {
	    				  bcIndex = ((ShrikeBTMethod) s.getNode().getMethod()).getBytecodeIndex(instructionIndex);
	    				  try {
	    					  int src_line_number = s.getNode().getMethod().getLineNumber(bcIndex);
	    					  System.err.println ( "Source line number = " + src_line_number );
	    				  } catch (Exception e) {
	        	      		  System.err.println("Bytecode index no good");
	    					  System.err.println(e.getMessage());
	    				  }
	    			  } catch (Exception e ) {
	        	    	  System.err.println("it's probably not a BT method (e.g. it's a fakeroot method)");
	    				  System.err.println(e.getMessage());
	    			  } 
	    	  }	
	      }
	  }
}
