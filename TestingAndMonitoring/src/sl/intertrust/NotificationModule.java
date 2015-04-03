package sl.intertrust;

public class NotificationModule {
	public static void enable_log_to_stdout(Boolean enable) {
		Log.enable_log_to_stdout(enable);
	}
	public static void connectMonitoringTool(String url, int serviceID) throws Exception {
		Log.connect_remote(url, serviceID);
	}
	
/**	Use values from sl.Const to construct mask.
 *  To log everything but method parameters and source lines, use xor: 
 *     mask = all ^ method_params ^ source_lines
 *  To log only source_line and object name: 
 *     mask = source_line | name
 */
	public static void log_only(long mask) {
		Log.setLogMask(mask);
	}
}
