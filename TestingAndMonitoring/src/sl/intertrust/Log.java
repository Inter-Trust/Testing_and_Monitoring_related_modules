package sl.intertrust;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;

public class Log {
	static LogBuffer log_buffer;
	static boolean stdout_enabled = false;
	static long log_mask = Const.all;
	static RemoteLogger remote = null; //connect_remote_logger();
	
	private static RemoteLogger connect_remote_logger(String url, int serviceID) {
		if(url==null) { 
			System.err.println("URL of remote logger is null");
			return null;
		}
		try {
			if (stdout_enabled) 
				System.out.println("Connecting to remote logger at "+url);
			return new RemoteLogger(url, serviceID);
		} catch(Exception e) {
			System.err.println("RemoteLogger could not be initialized. "+e);
		}
		return null;
	}
	
	// This is how the logger needs to be initialized (by the NotificationModule)
	static void connect_remote(String url, int serviceID) throws Exception {
		remote  = connect_remote_logger(url, serviceID);
		if(remote==null) throw new Exception("Could not connect remote logger at url: \"" + url + "\"");
	}
	
	// This is the second step that might need to be called (for debugging).
	static void enable_log_to_stdout(boolean enable) { stdout_enabled = enable; }
	

	private static void i(String eventName, String msg) { i(eventName, msg, null); }
    private static void i(String eventName, String msg, JoinPoint jp) { i(eventName, msg, jp, Log.getAttributeStore());	}
	private static void i(String eventName, String msg, JoinPoint jp, AttributeStore as) {
		if(jp != null) add_info(as, jp);
		String rest = as.toString();
		if(stdout_enabled) {
			if(!rest.equals("")) System.out.println(msg + " -- " + rest);
			else System.out.println(msg);
		}
		as.push_front(Const.log_msg, msg);
		if(log_buffer != null) log_buffer.log(as);
//TODO : ... should be 	"Log" for Test Init log, "NotifType" for Notification log	
		if(remote != null) remote.send(as, eventName);
	}
	
	public static void notif_attribute(String msg, JoinPoint jp, AttributeStore as) {
		as.push_back(Const.attribute_value, get_attribute_value(jp)); i("Notif", msg, jp, as);
	}
	
	public static void notif_ping(JoinPoint jp)                    {  i("Notif", "Ping", jp); }

	public static void notif_ping(JoinPoint jp, AttributeStore as) {  i("Notif", "Ping", jp, as); }
	
	public static void notif_method_enter (JoinPoint jp, AttributeStore as) {
		                                                              i("Notif", "Method Enter", jp, as);
	}
	public static void notif_method_leave (JoinPoint jp, AttributeStore as) {
        															  i("Notif", "Method Leave", jp, as);
	}
	public static void notif_testinit(String msg)                  {  i("Test Init Log", msg); }
	
	public static void notif_testinit(String msg, JoinPoint jp)    {  i("Test Init Log", msg, jp); }
	
	static String get_attribute_value(JoinPoint a) {
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
//		String module = def(sig.getDeclaringTypeName());
//		String name = def(sig.getName());
//		as.push_back(Const.name, module+"."+name);
		as.push_back(Const.name, def(sig.getDeclaringTypeName()) + "." + def(sig.getName()));
		
		SourceLocation so = jp.getSourceLocation();
//		String file = def(so.getFileName());
//		String line = def(Integer.toString(so.getLine()));
//		as.push_back(Const.source_line, file+":"+line);
		as.push_back(Const.source_line, def(so.getFileName()) + ":" + def(Integer.toString(so.getLine())));

	}	
	
	// return default value on null
	private static String def(String s) 		  { return def(s, "");	}
	private static String def(String s, String d) { return (s==null)?d:s; }

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
	
	static void setLogMask(long mask) { log_mask = mask; }
	public static long getLogMask() { return log_mask; }
	
	public static AttributeStore getAttributeStore() {	return getAttributeStore(log_mask); 	}
	public static AttributeStore getAttributeStore(long mask) {
		AttributeStore a = new AttributeStore();
		a.setLogMask(mask);
		return a;	
	}
}
