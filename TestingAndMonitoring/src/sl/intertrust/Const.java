package sl.intertrust;

import java.util.HashMap;

// Can be used with "import static" to skip namespace
public class Const {
	// 64 bits ought to be enough for everyone! ;)
	private static HashMap<Long, String> map = init();
	public static final long
		attribute_value = 1<<0,
		class_id = 1<<1,
		class_instances = 1<<2,
		duration = 1<<3,
		exception = 1<<4,
		exit_status = 1<<5,
		log_msg = 1<<6,
		name = 1<<7,
		method_params = 1<<8,
		return_value = 1<<9,
		source_line = 1<<10,
		thread_count = 1<<11,
		thread_name = 1<<12,
		time_stamp = 1<<13,
		tracked_objects = 1<<14,
		all = (1<<15)-1;
	
	static private HashMap<Long, String> init() {
		HashMap<Long, String> m = new HashMap<Long, String>();
		m.put(attribute_value, "attribute_value");
		m.put(class_id, "class_id");
		m.put(class_instances, "class_instances");
		m.put(duration, "duration[microsec]");
		m.put(exception, "exception");
		m.put(exit_status, "exit_status");
		m.put(log_msg, "msg");
		m.put(name, "name");
		m.put(method_params, "parameters");
		m.put(return_value, "return_value");
		m.put(source_line, "source");
		m.put(thread_count, "thread_count");
		m.put(thread_name, "thread_name");
		m.put(time_stamp, "time_stamp");
		m.put(tracked_objects, "tracked_objects");
		return m;
	}
	
	public static String toStr(long l) {
		return map.get(l);
	}
}
