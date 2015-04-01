package sl.intertrust;

import java.io.IOException;


public class TestInitModule {
	static TestInitDaemon rest;
	static public void startRestServer(int port) throws IOException {
		rest = new TestInitDaemon(port);
		rest.start();
	}
	public static Boolean ready() {
		return TestInit.singleton().ready();
	}
	public static void reset() {
		TestInit.singleton().reset();
	}
}
