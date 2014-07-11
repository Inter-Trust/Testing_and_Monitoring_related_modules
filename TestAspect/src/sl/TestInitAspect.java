package sl;

import java.io.IOException;

import sl.tools.Injector;

public class TestInitAspect {
	static Demon rest;
	static public void startRestServer(int port) throws IOException {
		rest = new Demon(port);
		rest.start();
	}
	public static Boolean ready() {
		return Injector.singleton().ready();
	}
	public static void reset() {
		Injector.singleton().reset();
	}
}
