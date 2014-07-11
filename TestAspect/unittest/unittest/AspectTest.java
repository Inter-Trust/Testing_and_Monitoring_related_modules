package unittest;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sl.TestInitAspect;
import sl.tools.AttributeStore;
import sl.tools.Counter;
import sl.tools.Injector;
import sl.tools.Log;
import sl.tools.LogBuffer;


public class AspectTest {
	static LogBuffer log;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		log = Log.log_to_buffer();
		Log.enable_log_stdout(true);
		TestInitAspect.startRestServer(8080);
	}

	@Before
	public void setUp() throws Exception { 
		log.clear();
		Counter.clear();
		}
	
//	@After
//	public void tearDown() throws Exception {
//		enable_globals(false);
//		Log.i("Test End"); 
//	}
	
	@Test
	public void basic_test() {
		System.out.println("hello");
	}
	
	@Test
	public void inject() throws Exception {
		@SuppressWarnings("unused")
		A7 a = new A7();
		Injector ca = Injector.singleton();
		ca.reset();
		ca.ready();
		//assertLog(1, "msg:A7 reset");
	}
	
	private void assertLog(int line, String attrs) {
		String[] a = attrs.split(",");
		try {
			AttributeStore logline = log.Entry(line);
			for(String i: a) logline = logline.Has(i.trim());
		} catch(Exception e) {
			System.out.println(e);
			fail();
		}
	}	
}
