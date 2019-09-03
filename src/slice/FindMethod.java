package slice;

import java.util.Iterator;

import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.util.debug.Assertions;
import com.ibm.wala.util.strings.Atom;

public class FindMethod {
	public static CGNode findMethod(CallGraph cg, String Name, String methodCLass) {
		if(Name.equals(null) && methodCLass.equals(null))	return null;
		Atom name = Atom.findOrCreateUnicodeAtom(Name);
		for (Iterator<? extends CGNode> it = cg.iterator(); it.hasNext();) {
			CGNode n = it.next();
			if (n.getMethod().getName().equals(name) ) {
				if(n.getMethod().getDeclaringClass().getName().toString().equals(methodCLass)) {
				
				return n;
			}
			}
		}
		Assertions.UNREACHABLE("Failed to find method " + name);
		return null;
	}
}
