package slice;

import java.io.IOException;

import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.WalaException;

public class SliceMain {
	public static void main(String args[]) throws IllegalArgumentException, IOException, InvalidClassFileException, CancelException, WalaException {
		String jar = "exam.jar";
		
		String callname = "getContentType"; 
		String callmethodClass = "Lexam/GetContentType";
//		String methodName = "println";
		
		System.out.println("=====  callname: "+callname+" === callmethodClass: "+callmethodClass+"  ======");
		
//		SliceFromStatement.sliceFromAPI(jar, callname, callmethodClass, methodName);
		SliceFromStatement.sliceFromReturn(jar, callname, callmethodClass);
		
		
//		DoSlicing.doSlicing(jar); //zyt:can not run 
	}
}
