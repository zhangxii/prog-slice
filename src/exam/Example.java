package exam;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
public class Example{
	  public static String readWebPage(URL url) {
		    {
		      
		      InputStream is1;
		      String s1;
		      StringBuilder sb1;
		      InputStreamReader isr1;
//		      AbstractStringBuilder asb1;
		      String s2 = null;
		      BufferedReader br1;
		      try {
		        is1 = url.openStream();
		        isr1 = new InputStreamReader(is1);
		        br1 = new BufferedReader(isr1);
		        sb1 = new StringBuilder();
		        while ((s1 = br1.readLine()) != null) {
//		          asb1 = sb1.append(s1);
		        	sb1 = sb1.append(s1);
		        }
		        s2 = sb1.toString();
		      } catch (IOException _e) {
		      }
		      return s2;
		    }
		  }
		
	
	public static String right_readWebPage(String newUrl) throws IOException{
		URL url=new URL(newUrl);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		String line;
		StringBuffer urlStr = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			urlStr = urlStr.append(line);
        }
        reader.close();
        return urlStr.toString();
	}
	
	
	
   public static void main(String [] args) throws IOException   {
	   String urlStr = "http://www.runoob.com";
	   URL url = new URL(urlStr);
	   System.out.println(right_readWebPage(urlStr));
	   System.out.println(readWebPage(url));    
   }
}