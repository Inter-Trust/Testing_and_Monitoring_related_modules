package sl.intertrust;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.WeakHashMap;

import sl.intertrust.annotations.Testing;

public class TestInit {
	enum Funcs { ready, reset };
	WeakHashMap<Object, String> reset_host = new WeakHashMap<Object, String>();
	WeakHashMap<Object, String> ready_host = new WeakHashMap<Object, String>();
	private static TestInit center = new TestInit();
	
	private TestInit() {};
	public static TestInit singleton() { return center;	}
	public void add_reset_function(Object obj) { add_function(reset_host, obj, Funcs.reset); }
	public void add_ready_function(Object obj) { add_function(ready_host, obj, Funcs.ready); }
	public void reset() { callAll(reset_host); }
	
	public Boolean ready() {
		Boolean acc = true;
		for(Object b: callAll(ready_host)) {
			if( b == null) {
				Log.notif_testinit("TestInitModule.ready: ready function returned null");
				continue;
			}
			acc = acc && (Boolean) b;
		}
		return acc;
	}

	void add_function(WeakHashMap<Object, String> host, Object obj, Funcs f) {
		String method = get_method(obj, f);
		if(method != null) host.put(obj, method); 
		else System.err.println(String.format("Error, class %s has no valid %s function!", obj.getClass().getName(), f.toString()));
	}

	ArrayList<Object> callAll(WeakHashMap<Object, String> func_map) {
		ArrayList<Object> acc = new ArrayList<Object>();
		for(Object o: func_map.keySet()) 
			acc.add(call(o, func_map.get(o)));
		return acc;
	}
	
	private String get_method(Object obj, Funcs func) {
		Annotation[] ann = obj.getClass().getAnnotations();
		for(Annotation a: ann) {
			if(!(a instanceof Testing)) continue;
			Testing o = (Testing) a;
			switch(func) {
				case ready:	return o.ready();
				case reset: return o.reset();
			}
		}
		return null; //Annotation not found
	}
	
	Object call(Object obj, String method_name) {
		try {
			java.lang.reflect.Method method = obj.getClass().getMethod(method_name);
			if(method != null) return method.invoke(obj);
		} catch (Exception e) {	System.err.println("Error " + e); }
		return null;
	}
	
	public boolean hasAnnotation(Object obj) {
		Annotation[] ann = obj.getClass().getAnnotations();
		for(Annotation a: ann) 
			if(a instanceof Testing) return true; 
		return false;
	}
}
