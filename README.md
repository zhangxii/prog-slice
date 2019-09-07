# prog-slice
Using wala to slice the java code.

Before running the code, you should open the com.ibm.wala.core-1.5.3.jar as a compressed package, then open the wala.properties file and modified the java path :

`java_runtime_dir = your_java_path`

for example:`/usr/lib/jvm/jdk1.8.0_101/jre/lib/`

Afterwards, you can use the `src/slice/SliceMain.java` to slice a java code.

Attention: The code you want to slice must be compiled to a jar package.

