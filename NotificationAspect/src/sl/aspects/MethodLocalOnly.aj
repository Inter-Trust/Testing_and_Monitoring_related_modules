package sl.aspects;

import sl.MonitorOnly;
import sl.tools.MethodDuration;

public aspect MethodLocalOnly extends Base {
	pointcut methods(MonitorOnly m): execution(@MonitorOnly * *(..)) && @annotation(m); // Methods with @Monitor annotation
	pointcut classes(MonitorOnly m): execution(* *(..)) && within(@MonitorOnly *) && @annotation(m); //All methods within annotated class
	pointcut monitor(MonitorOnly m): (methods(m) || classes(m)) && scope();
	
	
	Object around(MonitorOnly m) : monitor(m) {
		MethodDuration md = new MethodDuration(thisJoinPoint, m.mask());
		Object return_value = null;
		boolean success = false;
		try{
			return_value = proceed(m);
			success = true;
			return return_value;
		} catch(Exception e) {		
			md.set_exception(e);
		} finally {
			md.on_end(thisJoinPoint, success, return_value);
		}
		return null;
	}
}
