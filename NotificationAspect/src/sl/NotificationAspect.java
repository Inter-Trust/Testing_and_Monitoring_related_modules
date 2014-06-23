package sl;

import sl.tools.Log;

public class NotificationAspect {
	public static void enable_log_to_stdout(Boolean enable) {
		Log.enable_log_stdout(true);
	}
	public static void connectMonitoringTool(String url) throws Exception {
		Log.connect_remote(url);
	}
	
	/**	 Use values from sl.Const to construct mask.
	 To log everything but method parameters and source lines, use xor: 
			mask = all ^ method_params ^ source_lines
	 To log only source_line and object name: 
	 		mask = source_line | name */
	public static void log_only(long mask) {
		Log.setLogMask(mask);
	}
}
