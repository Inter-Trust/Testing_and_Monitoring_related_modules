package sl.tools;

import java.util.HashMap;
import java.util.WeakHashMap;

import sl.Const;

// Counts number of object instances/class. 
// Garbage collected objects are automatically removed.
public class Counter {
	// Each type is tracked by a different hashmap
	@SuppressWarnings("serial")
	static class Map extends HashMap<String, WeakHashMap<Object, Integer>> {
		public void add(Object o, String class_id) {
			WeakHashMap<Object, Integer> key = get(class_id);
			if(key == null) put(class_id, new WeakHashMap<Object, Integer>());
			get(class_id).put(o, 0);
		}
		
		public void dump(AttributeStore as) {
			int sum = 0;
			for(String key: keySet()) {
				int n = get(key).size();
				sum += n;
				as.push_back(Const.class_id, key);
				as.push_back(Const.class_instances, n);
				//as.push_back(String.format("count(%s)", key), n);
			}
			if(sum>0) as.push_back(Const.tracked_objects, sum);
		}
	}
	
	static Counter counter = new Counter();
	static Map map = new Map();
	private Counter() {}
	public static Counter singleton() {	return counter;	}
	public void add(Object ref, String ref_class_id) {	map.add(ref, ref_class_id);	}
	public static void log(AttributeStore as) {	map.dump(as);	}
	public static void clear() {map.clear();}
}
