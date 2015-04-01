package sl.intertrust;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

//import org.apache.commons.io.IOUtils;




import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;


/**
* An example of subclassing NanoHTTPD to make a custom HTTP server.
*/
public class TestInitDaemon extends NanoHTTPD {
	final String RESET = "/reset";
	final String LOG = "/log";
	final String READY = "/ready";
	
    public TestInitDaemon(int port) {
        super(port);
    }

    @Override 
    public Response serve(IHTTPSession session) {
        String uri = session.getUri().toLowerCase();
        //Method method = session.getMethod();
        //System.out.println(method + " '" + uri + "' ");

        if(uri.equals(RESET)) return reset(session);
        if(uri.equals(LOG))	return log(session);
        if(uri.equals(READY)) return status(session);
        
        return new Response(Status.BAD_REQUEST, MIME_HTML, "Invalid entry point\nValid entry points are: \t/reset (GET request)\n\t/ready (GET request)\n\t/log (POST request, takes a log message as string)");
    }
    
    Response reset(IHTTPSession session) {
    	TestInitModule.reset();
    	return new Response("Target system reset requested.");
    }
    
    Response status(IHTTPSession session) {
    	if(TestInitModule.ready()) 
    		return new Response("READY");
    	else 
    		return new Response("NOT READY");
    }
    
   Response log(IHTTPSession session)  {
//    	if( !session.getMethod().equals(Method.POST))
//    		return method_not_allowed();
//        Map<String, String> files = new HashMap<String, String>();
//        Method method = session.getMethod();
//        if (Method.POST.equals(method)) {
//            try {
//                session.parseBody(files);
//                String postData = files.get("postData");
//                System.out.println(postData);
//            } catch (IOException ioe) {
//                return new Response(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
//            } catch (ResponseException re) {
//                return new Response(re.getStatus(), MIME_PLAINTEXT, re.getMessage());
//            }
//        }

        Map<String, String> files = new HashMap<String, String>();
        Method method = session.getMethod();
    	if( !method.equals(Method.POST))
    		return new Response(Status.METHOD_NOT_ALLOWED, MIME_HTML, "POST expected");
    	else {
            try {
                session.parseBody(files);
                String postData = files.get("postData");
                Log.notif_testinit(postData);
                return new Response("Log added.");
            } catch (IOException ioe) {
                return new Response(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
            } catch (ResponseException re) {
                return new Response(re.getStatus(), MIME_PLAINTEXT, re.getMessage());
            }
        }
    }
    
//    String toStr(InputStream is) throws IOException {
//    	//'org.apache.commons:commons-io:1.3.2'
//    	StringWriter writer = new StringWriter();
//    	IOUtils.copy(is, writer, "utf8");
//    	String theString = writer.toString();    
//    	return theString;
//    }
    
/// the above needs org.apache.commons:commons-io:1.3.2... BS, just for this one IOUtils operation
/// the below is copied from http://stackoverflow.com/a/14107694 in good faith that it works...
   
   String toStr(InputStream in) throws IOException {
       BufferedReader reader = new BufferedReader(new InputStreamReader(in));
       StringBuilder out = new StringBuilder();
       String newLine = System.getProperty("line.separator");
       String line;
       while ((line = reader.readLine()) != null) {
           out.append(line);
           out.append(newLine);
       }
       return out.toString();
   }
   
   String http_get(String method, String url) throws IOException {
    	URL url2 = new URL(url);
    	HttpURLConnection connection = (HttpURLConnection)url2.openConnection();
    	connection.setRequestMethod(method);
    	connection.setDoOutput(true);
    	try {
			connection.connect();
		} catch (IOException e) {
			return e.toString();
		}
    	InputStream stream = connection.getInputStream();
    	return toStr(stream);
    }
    
    String http_post(String url, String data) throws IOException {
    	URL url2 = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty( "Content-Type", "text/plain" );
		byte[] encoded = data.getBytes("UTF-8"); 
		conn.setRequestProperty( "Content-Length", String.valueOf(encoded.length));
		OutputStream os = conn.getOutputStream();
		os.write(encoded); 
		os.flush(); 
		os.close();
    	return toStr(conn.getInputStream());
    }
}