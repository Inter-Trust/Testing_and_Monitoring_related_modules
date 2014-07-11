package sl;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import sl.tools.Log;


import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;


/**
* An example of subclassing NanoHTTPD to make a custom HTTP server.
*/
public class Demon extends NanoHTTPD {
	final String RESET = "/reset";
	final String LOG = "/log";
	final String READY = "/ready";
	//final String ID = "id";
	
    public Demon(int port) {
        super(port);
    }

    @Override 
    public Response serve(IHTTPSession session) {
        String uri = session.getUri().toLowerCase();
        Method method = session.getMethod();
        //System.out.println(method + " '" + uri + "' ");

        if(uri.equals(RESET)) return reset(session);
        if(uri.equals(LOG))	return log(session);
        if(uri.equals(READY)) return status(session);
//        switch(uri) {
//	        case RESET: return reset(session); 
//	        case LOG: 	return log(session); 
//	        case READY: return status(session); 
//        }
        
        return warning("Invalid command");
    }
    
    Response reset(IHTTPSession session) {
    	TestInitAspect.reset();
    	return new Response("Reset done.");
    }
    
    Response log(IHTTPSession session)  {
    	if( !session.getMethod().equals(Method.POST))
    		return method_not_allowed();
        Map<String, String> files = new HashMap<String, String>();
        Method method = session.getMethod();
        if (Method.POST.equals(method)) {
            try {
                session.parseBody(files);
                String postData = files.get("postData");
                Log.i(postData);
            } catch (IOException ioe) {
                return new Response(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
            } catch (ResponseException re) {
                return new Response(re.getStatus(), MIME_PLAINTEXT, re.getMessage());
            }
        }
    	return new Response("Log added.");
    }
    
    String toStr(InputStream is) throws IOException {
    	//'org.apache.commons:commons-io:1.3.2'
    	StringWriter writer = new StringWriter();
    	IOUtils.copy(is, writer, "utf8");
    	String theString = writer.toString();    
    	return theString;
    }
    
//    void print(Object o) {
//    	System.out.println(o);
//    }
    
    Response status(IHTTPSession session) {
    	if(TestInitAspect.ready()) return new Response("READY");
    	else return new Response("NOT READY");
    }
    
    Response method_not_allowed() {
    	return new Response(Status.METHOD_NOT_ALLOWED, MIME_HTML, "POST expected");
    }
    
    Response warning(String msg) {
//    	print(msg);
    	return new Response(Status.BAD_REQUEST, MIME_HTML, msg);
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