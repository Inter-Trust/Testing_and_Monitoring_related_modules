package sl.aspects;

import sl.MonitorMask;
import sl.tools.MethodDuration;

public aspect MethodLocalMask extends Base {
	pointcut methods(MonitorMask m): execution(@MonitorMask * *(..)) && @annotation(m); // Methods with @Monitor annotation
	pointcut classes(MonitorMask m): execution(* *(..)) && within(@MonitorMask *) && @annotation(m); //All methods within annotated class
	pointcut monitor(MonitorMask m): (methods(m) || classes(m)) && scope();
	
	
	Object around(MonitorMask m) : monitor(m) {
		MethodDuration md = new MethodDuration(thisJoinPoint);
		System.out.println(m);
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
