package unittest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sl.Const;
import sl.NotificationAspect;
//import sl.aspects.AttributeGlobal;
//import sl.aspects.CountGlobal;
//import sl.aspects.MethodGlobal;
//import sl.aspects.PingGlobal;
import sl.tools.AttributeStore;
import sl.tools.Counter;
//import sl.tools.Injector;
import sl.tools.LogBuffer;
import sl.tools.Log;

import data.A1;
import data.A2;
import data.A2Masked;
import data.A3;
import data.A4;
import data.A5;
import data.A6;
import data.MaskTest;
import static sl.Const.*;

public class AnnotationTest {
	static LogBuffer log;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		log = Log.log_to_buffer();
		NotificationAspect.enable_log_to_stdout(true);
		// NotificationAspect.connectMonitoringTool("http://localhost:4567/");
		// NotificationAspect.log_only(all);
	}

	@Before
	public void setUp() throws Exception {
		log.clear();
		Counter.clear();
		NotificationAspect.log_only(all);
	}

	@After
	public void tearDown() throws Exception {
		enable_globals(false);
		Log.i("Test End");
	}
	
	// Restore unity of tokens surrounded by [], from token stream split on ','
	//   (c[a,) (b])  -> (c[a, b])
	private String[] group_parens(String[] vec) {
		List<String> out = new LinkedList<String>();
		boolean inside_parens = false;
		StringBuilder sb = new StringBuilder();
		for(String i: vec) { // State: inside pharenteses
			i = i.trim();
			if(inside_parens) {
				if(i.contains("]")) { // Transition back to default state
					sb.append(", ");
					sb.append(i);
					out.add(sb.toString());
					sb.setLength(0);
					inside_parens = false;
				} else { // accumulate 
					sb.append(", ");
					sb.append(i);
				}
			} else { // State: outside of pharenteses
				if(i.contains("[") && !i.contains("]")) { // transition to accumulating
					sb.setLength(0);
					sb.append(i);
					inside_parens = true;
				} else { // normal branch
					out.add(i); 
				}
			}
		}
		return out.toArray(new String[out.size()]);
	}
	private void assertLog(int line, String attrs) {
		String[] a = group_parens(attrs.split(","));
		try {
			AttributeStore logline = log.Entry(line);
			for (String i : a)
				logline = logline.Has(i.trim());
		} catch (Exception e) {
			System.out.println(e);
			fail();
		}
	}

	private void assertException(int line, String attrs, String exception_text) {
		String[] a = group_parens(attrs.split(","));
		String res = null;
		try {
			AttributeStore logline = log.Entry(line);
			for (String i : a)
				logline = logline.Has(i.trim());
			// throw new Exception("Exception expected: "+exception_text);
		} catch (Exception e) {
			res = e.toString();
		}
		assertEquals(exception_text, res);
	}

	private void non_annotated_code() {
		A4 b = new A4();
		b.ping();
		b.outer();
		b.change_variables();
	}

	@Test
	public void monitor_method_attribute() throws Exception {
		A1 a = new A1();
		try {
			a.outer(); // should produce exit_status:fail
		} catch (Exception e) {
		}
		a.ping(); //
		a.params("hello", 12);
		a.change_variables(); // not monitored, should generate no log
		non_annotated_code(); // should produce no logs
		int i = 0;
		assertLog(i++, "msg:Method Enter, name:data.A1.outer");
		assertLog(i++, "msg:Method Leave, name:data.A1.outer");
		assertLog(i++, "msg:Ping, name:data.A1.ping");
		assertException(i++, "msg:Method Enter, parameters:[hello, 12]", "java.lang.Exception: parameters:[hello, 12] not found");
		assertLog(i++, "msg:Method Leave, name:data.A1.params, return_value:12");
		assertLog(i++,
				"msg:Attribute changed, attribute_value:1, name:data.A1.variableA");
		assertEquals(log.size(), 6);
	}

	@Test
	public void monitor_method_attribute_no_more() throws Exception {
		//NotificationAspect.log_only(all);
		NotificationAspect.log_only(all ^ source_line ^ return_value^ method_params);
		System.out.println("monitor_method_attribute_no_more");
		A1 a = new A1();
		try {
			a.outer(); // should produce exit_status:fail
		} catch (Exception e) {
		}
		a.ping(); //
		a.params("hello", 12);
		NotificationAspect.log_only(all ^ source_line ^ return_value^ method_params);
		a.params2("hello", 12);
		NotificationAspect.log_only(all);
		a.change_variables(); // not monitored, should generate no log
		non_annotated_code(); // should produce no logs
		int i = 0;
		assertLog(i++, "msg:Method Enter, name:data.A1.outer");
		assertLog(i++, "msg:Method Leave, name:data.A1.outer");
		assertLog(i++, "msg:Ping, name:data.A1.ping");
		assertException(i++, "parameters:[hello, 12]",
				"java.lang.Exception: parameters:[hello, 12] not found");
		assertLog(i++, "return_value:12");
		assertException(i++, "parameters:[hello, 12]",
				"java.lang.Exception: parameters:[hello, 12] not found");	
		i++;
		assertLog(i++,
				"msg:Attribute changed, attribute_value:1, name:data.A1.variableA");
		assertEquals(log.size(), 8);
	}

	@Test
	public void ping() throws Exception {
		A5 a5 = new A5();

		a5.ping();
		a5.pingonly();
		int i = 0;
		assertLog(i++, "name:data.A5.ping");
		assertException(i++, "name:data.A5.pingonly",
				"java.lang.Exception: name:data.A5.pingonly not found");
		assertEquals(log.size(), 2);
	}

	@Test
	public void monitor_mask() throws Exception {
		MaskTest a = new MaskTest();
		a.outer();
		// a.ping(); //
		a.params("hello", 12);
		a.change_variables(); // not monitored, should generate no log
		non_annotated_code(); // should produce no logs
		int i = 0;
		assertLog(i++, "msg:Method Enter, name:data.MaskTest.outer, thread_name:main");
		assertLog(i++, "msg:Method Leave, exit_status:success, return_value:null, name:data.MaskTest.outer, thread_name:main, thread_count:2");
		assertLog(i++, "msg:Method Enter, name:data.MaskTest.params");
		assertLog(i++, "msg:Method Leave, name:data.MaskTest.params");
		assertLog(i++, "msg:Method Enter , parameters:[hello, 14]");//, name:data.MaskTest.inner2, thread_name:main, thread_count:2");
		assertLog(i++, "msg:Method Leave, exit_status:success, return_value:null, name:data.MaskTest.inner2, source:MaskTest.java:26, thread_name:main, thread_count:2");
		assertLog(i++, "msg:Attribute changed, attribute_value:1, name:data.MaskTest.variableA, thread_name:main, thread_count:2");
		assertException(i++, "msg:Attribute changed, attribute_value:2, name:data.MaskTest.variableB", "java.lang.Exception: attribute_value:2 not found");
		assertEquals(log.size(), 8);
	}

	@Test
	public void taint() throws Exception {
		A2 a = new A2();
		a.outer();
		a.taint();

		non_annotated_code();
		NotificationAspect.log_only(all ^ name);
		a.taint();
		
		int i = 0;
		assertLog(i++, "msg:Method Enter, name:data.A2.taint");
		assertLog(i++, "msg:Method Enter, name:data.A2.inner");
		assertLog(i++, "msg:Method Leave, name:data.A2.inner");
		assertLog(i++, "msg:Method Leave, name:data.A2.taint");		
		
		assertException(i++, "msg:Method Enter, name:data.A2.taint", "java.lang.Exception: name:data.A2.taint not found");
		assertException(i++, "msg:Method Enter, name:data.A2.inner", "java.lang.Exception: name:data.A2.inner not found");
		assertException(i++, "msg:Method Leave, name:data.A2.inner", "java.lang.Exception: name:data.A2.inner not found");
		assertException(i++, "msg:Method Leave, name:data.A2.taint", "java.lang.Exception: name:data.A2.taint not found");
				
		assertEquals(log.size(), 8);		
	}

	@Test
	public void taint_only() throws Exception {
		A2Masked a = new A2Masked();
		a.outer("password_outer");
		a.taint("password");
		// non_annotated_code();
		assertException(
				0,
				"msg:Method Enter, name:data.A2Masked.taint, parameters:[password]",
				"java.lang.Exception: parameters:[password] not found");
		assertException(
				1,
				"msg:Method Enter, name:data.A2Masked.inner, parameters:[password]",
				"java.lang.Exception: parameters:[password] not found");
		assertLog(2, "msg:Method Leave, name:data.A2Masked.inner");
		assertLog(3, "msg:Method Leave, name:data.A2Masked.taint");
		assertEquals(log.size(), 4);
	}

	@Test
	public void monitor_class() throws Exception {
		A3 a = new A3();
		a.ping();
		a.outer();
		a.change_variables();
		non_annotated_code();
		NotificationAspect.log_only(all^attribute_value);
		a.change_variables();
		
		int i = 0;
		assertLog(i++, "msg:Method Enter, name:data.A3.ping");
		assertLog(i++, "msg:Method Leave, exit_status:success");
		assertLog(i++, "msg:Method Enter, name:data.A3.outer");
		assertLog(i++, "msg:Method Leave, exit_status:success, name:data.A3.outer");
		assertLog(i++, "msg:Method Enter, name:data.A3.change_variables");
		assertLog(i++, "msg:Attribute changed, attribute_value:1, name:data.A3.variableA");
		assertLog(i++, "msg:Method Leave, name:data.A3.change_variables");
		
		assertLog(i++, "msg:Method Enter, name:data.A3.change_variables");
		assertException(i++, "msg:Attribute changed, attribute_value:1, name:data.A3.variableA", "java.lang.Exception: attribute_value:1 not found");
		assertLog(i++, "msg:Method Leave, name:data.A3.change_variables");
		
		assertEquals(log.size(), 10);
	}

	@Test
	public void count() throws Exception {
		// Log.enable_stdout(false);
		List<A5> as = new ArrayList<A5>();
		List<A6> a6s = new ArrayList<A6>();
		for (int i = 0; i < 10; i++)
			as.add(new A5());
		for (int i = 0; i < 5; i++)
			a6s.add(new A6());
		as.get(0).ping();
		assertLog(
				0,
				"class_id:data.A5, class_instances:10, class_id:data.A6, class_instances:5, tracked_objects:15");
	}

	private void enable_globals(boolean b) {
	}
}
