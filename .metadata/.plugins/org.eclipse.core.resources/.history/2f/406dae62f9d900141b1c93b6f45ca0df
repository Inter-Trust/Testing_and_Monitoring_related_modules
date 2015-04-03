package main;

import sl.intertrust.NotificationModule;
import sl.intertrust.TestInitModule;

public class Main {
	
	public static void main(String[] args) throws Exception {
		NotificationModule.connectMonitoringTool("http://localhost:4567", 123);
		NotificationModule.enable_log_to_stdout(true);
		TestInitModule.startRestServer(8081);

		System.out.println("Thiss is just a default test if connections work...");
		Helper h = new Helper();
		h.f();
		h.g();
		System.out.println("Check the Notification output on the MMT or its placeholder.\nAlso try the Test Init module.\nPress a RETURN to quit.");
		System.in.read();
		System.out.println("Alright, we're done.");
	}

}
