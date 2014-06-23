package main;

import sl.Monitor;
import sl.MonitorOnly;
import sl.NotificationAspect;
import sl.tools.Log;

public class Main {
	static class A2 {
		@MonitorOnly(mask=1)
		public void f() {}
		@Monitor
		public void g() {};
	}
	public static void main(String[] args) throws Exception {
		NotificationAspect.enable_log_to_stdout(true);
		//NotificationAspect.connectMonitoringTool("http://localhost:8080");
		Log.i("hello bro");
		A2 a = new A2();
		a.f();
		//a.g();
		System.out.println("done");
	}

}
