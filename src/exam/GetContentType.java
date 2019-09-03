package exam;
import java.net.MalformedURLException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
public class GetContentType {
	  public static String getContentType(String url) throws IOException {
		    {
		      int i1;
		      URLConnection urlc1;
		      String s1 = null;
		      URL url1;
		        url1 = new URL(url);
		        urlc1 = url1.openConnection();
		        i1 = urlc1.getContentLength();
		        s1 = urlc1.getContentType();
		      return s1;
		    }
		  }		
}





