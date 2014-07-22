package sl.tools;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;
import sl.Const;


public class Log {
	static LogBuffer log_buffer;
	static boolean stdout_enabled = false;
	static long log_mask = Const.all;
	//static String remote_logger_url = null;
	static RemoteLogger remote = null; //connect_remote_logger();
	
	// Java has no default value parameters :-(
	public static void i(String msg) { i(msg, null); }
	public static void i(String s, JoinPoint jp) { i(s, jp, Log.getAttributeStore());	}
	public static void i(String msg, JoinPoint jp, AttributeStore as) {
		if(jp != null) add_info(as, jp);
		String rest = as.toString();
		if(stdout_enabled) {
			if(!rest.equals("")) System.out.println(msg + " -- " + rest);
			else System.out.println(msg);
		}
		as.push_front(Const.log_msg, msg);
		if(log_buffer != null) log_buffer.log(as);
		if(remote != null) {
			boolean connection_ok = remote.log(as);
			if(!connection_ok) remote = null; // Drop the remote logger functionality.
			// Otherwise we have a delay penalty on each retry.
		}
	}
	
	// Deprecated, use NotificationAspect.enable_log_to_stdout(true) as API
	static public void enable_log_stdout(boolean enable) { stdout_enabled = enable; }
	
	// Deprecated, use NotificationAspect.connectMonitoringTool("http://localhost:4567/") as API	
	static public void connect_remote(String url) throws Exception {
		remote  = connect_remote_logger(url);
		if(remote==null) throw new Exception("Could not connect remote logger at url: "+url);
	}
	
	static private RemoteLogger connect_remote_logger(String url) {
		if(url==null) { 
			Log.i("URL of remote logger is null");
			return null;
		}
		try {
			Log.i("Connecting to remote logger at "+url);
			return new RemoteLogger(url);
		} catch(Exception e) {
			Log.i("RemoteLogger could not be initialized. "+e);
		}
		return null;
	}
	
	public static void log_attribute(String msg, JoinPoint jp, AttributeStore as) {
		//AttributeStore as = Log.getAttributeStore();
		as.push_back(Const.attribute_value, get_attribute_value(jp));
		i(msg, jp, as);
	}
	
	public static String get_attribute_value(JoinPoint a) {
		Object[] args = a.getArgs();
		if(args==null || args.length <1) return "";
		return args[0].toString();
	}

	private static void add_info(AttributeStore as, JoinPoint jp) {
		add_source_location(as, jp);
		as.push_back(Const.thread_name, Thread.currentThread().getName());
		as.push_back(Const.thread_count, Thread.activeCount());
		as.push_back(Const.time_stamp, System.currentTimeMillis());
		Counter.log(as);
	}
	
	static void add_source_location(AttributeStore as, JoinPoint jp) {
		Signature sig = jp.getSignature();
		String module = def(sig.getDeclaringTypeName());
		String name = def(sig.getName());
		as.push_back(Const.name, module+"."+name);
		
		SourceLocation so = jp.getSourceLocation();
		String file = def(so.getFileName());
		String line = def(Integer.toString(so.getLine()));
		as.push_back(Const.source_line, file+":"+line);
	}	
	
	// return default value on null
	static private String def(String s) 		   { return def(s, "");	}
	static private String def(String s, String d ) { return (s==null)?d:s;	}

	static void get_parameters(JoinPoint a, AttributeStore as) {
		Object[] args = a.getArgs();
		if(args == null || args.length == 0) return;
		String params = StringUtils.join(args,", ");
		String buf = String.format("[%s]", params);
		as.push_back(Const.method_params, buf);
	}
	
	static void get_return_value(JoinPoint a, AttributeStore as) {
		Object[] args = a.getArgs();
		if(args == null || args.length == 0) return;
		as.push_back(Const.return_value, args[0]);
	}
	
	public static LogBuffer log_to_buffer() {
		log_buffer = new LogBuffer();
		return log_buffer;
	}
	
	public static void setLogMask(long mask) { 	log_mask = mask; }
	public static long getLogMask() {return log_mask;}
	
	public static AttributeStore getAttributeStore() {	return getAttributeStore(log_mask); 	}
	public static AttributeStore getAttributeStore(long mask) {
		AttributeStore a = new AttributeStore();
		a.setLogMask(mask);
		return a;	
	}
}
