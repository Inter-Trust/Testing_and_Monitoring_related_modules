package unittest;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import data.A7;
import sl.intertrust.Counter;
import sl.intertrust.Log;
import sl.intertrust.LogBuffer;
import sl.intertrust.TestInit;
import sl.intertrust.TestInitModule;
import sl.intertrust.NotificationModule;


public class TestInitTest {
	static LogBuffer log;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		log = Log.log_to_buffer();
		NotificationModule.enable_log_to_stdout(true);
		TestInitModule.startRestServer(8080);
	}

	@Before
	public void setUp() throws Exception { 
		log.clear();
		Counter.clear();
		}
	
//	@After
//	public void tearDown() throws Exception {
//		enable_globals(false);
//		System.out.println("Test End"); 
//	}
	
	@Test
	public void basic_test() {
		System.out.println("hello");
	}
	
	@Test
	public void inject() throws Exception {
		@SuppressWarnings("unused")
		A7 a = new A7();
		TestInit ca = TestInit.singleton();
		ca.reset();
		ca.ready();
		//assertLog(1, "msg:A7 reset");
	}
	
//	private void assertLog(int line, String attrs) {
//		String[] a = attrs.split(",");
//		try {
//			AttributeStore logline = log.Entry(line);
//			for(String i: a) logline = logline.Has(i.trim());
//		} catch(Exception e) {
//			System.err.println(e);
//			fail();
//		}
//	}	
}
