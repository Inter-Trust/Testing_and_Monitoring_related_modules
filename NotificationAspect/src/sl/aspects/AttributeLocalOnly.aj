package sl.aspects;

import org.aspectj.lang.JoinPoint;

import sl.MonitorOnly;
import sl.tools.AttributeStore;
import sl.tools.Log;

public aspect AttributeLocalOnly extends Base {
//	pointcut classes(MonitorOnly m): set(* *) && within(@MonitorOnly *) && @annotation(m); // All attribute setters within @Monitor annotated class
//	pointcut attributes(MonitorOnly m) : set(@MonitorOnly * *) && @annotation(m);
//	pointcut probe(MonitorOnly m): (attributes(m) && classes(m)) && scope();
	// All attribute setters within @Monitor annotated class
	/** Usage:
	 * @MonitorOnly(7) 
	 * class A {
	 *  	long a;
	 *  	String b;
	 *  }
	 * Or
	 * class A {
	 * 	@MonitorOnly(15) long a;
	 *  @MonitorOnly(7) String b;
	 * }
	 * 
	 */
	pointcut probe(MonitorOnly m): ((set(* *) && within(@MonitorOnly *)) || set(@MonitorOnly * *)) && @annotation(m) && scope();

	before(MonitorOnly m) : probe(m) {
		AttributeStore as = Log.getAttributeStore(m.mask());
		Log.i("Attribute changed", thisJoinPoint, as);
	}	
}