package main;

import sl.intertrust.NotificationModule;
import sl.intertrust.TestInitModule;

public class Main {
	
	public static void main(String[] args) throws Exception {
		System.out.println("To not see MMT Connector (network connection) errors, please start a MMT tool (mockup) on localhost:4567.");
		NotificationModule.connectMonitoringTool("http://localhost:4567", 123);
		NotificationModule.enable_log_to_stdout(true);
		System.out.println("Test init REST server will be started on localhost:8081");
		TestInitModule.startRestServer(8081);

		System.out.println("This is just a default test if connections work...");
		MainHelper h = new MainHelper();
		h.f();
		h.g();
		System.out.println("Check the Notification output on the MMT or its mockup.\nAlso try the Test init module.\nPress a RETURN to quit.");
		System.in.read();
		System.out.println("Alright, we're done.");
	}
}
