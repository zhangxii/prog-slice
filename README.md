# prog-slice
Using wala to slice the java code.
Open the com.ibm.wala.core-1.5.3 as a compressed package, open the wala.properties file and modified the `java_runtime_dir = your_java_path`,for example:`/usr/lib/jvm/jdk1.8.0_101/jre/lib/`before you run the code.

You can use the `src/slice/SliceMain.java` to slice a java code.

Attention: The code you want slice must be compiled to a jar package.

