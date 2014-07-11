package sl.tools;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.WeakHashMap;
import sl.Inject;

public class Injector {
	enum Funcs { ready, reset };
	WeakHashMap<Object, String> reset_host = new WeakHashMap<Object, String>();
	WeakHashMap<Object, String> ready_host = new WeakHashMap<Object, String>();
	private static Injector center = new Injector();
	
	private Injector() {};
	public static Injector singleton() { return center;	}
	public void add_reset_function(Object obj) { add_function(reset_host, obj, Funcs.reset); }
	public void add_ready_function(Object obj) { add_function(ready_host, obj, Funcs.ready); }
	public void reset() { callAll(reset_host); }
	
	public Boolean ready() {
		Boolean acc = true;
		for(Object b: callAll(ready_host)) {
			if( b == null) {
				Log.i("TestInitAspect.ready: ready function returned null");
				continue;
			}
			acc = acc && (Boolean) b;
		}
		return acc;
	}

	void add_function(WeakHashMap<Object, String> host, Object obj, Funcs f) {
		String method = get_method(obj, f);
		if(method != null) host.put(obj, method); 
		else Log.i(String.format("Error, class %s has no valid %s function!", obj.getClass().getName(), f.toString()));
	}

	ArrayList<Object> callAll(WeakHashMap<Object, String> func_map) {
		ArrayList<Object> acc = new ArrayList<Object>();
		for(Object o: func_map.keySet()) 
			acc.add(call(o, func_map.get(o)));
		return acc;
	}
	
	private String get_method(Object obj, Funcs func) {
		System.out.println("get method of class "+obj.getClass().getName());
		Annotation[] ann = obj.getClass().getAnnotations();
		System.out.println("Number of annotations " + ann.length);
		for(Annotation a: ann) {
			if(!(a instanceof Inject)) continue;
			Inject o = (Inject) a;
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
		} catch (Exception e) {	Log.i("Error " + e); }
		return null;
	}
	
	public boolean hasAnnotation(Object obj) {
		Annotation[] ann = obj.getClass().getAnnotations();
		// System.out.println("Number of annotations " + ann.length);
		for(Annotation a: ann) 
			if(a instanceof Inject) return true; 
		return false;
	}
}
