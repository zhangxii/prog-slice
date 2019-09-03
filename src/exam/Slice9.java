package exam;

import java.util.Random;
//test succeed
public class Slice9 {

	  public static void main(String[] args) {
	    Random r = null;
	    r = new Random();
	    int i = 42;
	    i = r.nextInt();
	    int j;
	    j = 42*i;
	    doNothing(j);
	  }
	  
	  static void doNothing(int x) {}
	}