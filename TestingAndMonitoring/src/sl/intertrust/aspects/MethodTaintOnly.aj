package sl.intertrust.aspects;

import sl.intertrust.MethodDuration;
import sl.intertrust.annotations.TaintOnly;

public aspect MethodTaintOnly extends Base {
	pointcut methods(TaintOnly m): execution(@TaintOnly * *(..)) && @annotation(m); // Methods with @Monitor annotation
	pointcut monitor(TaintOnly m): cflow(methods(m)) && execution(* *(..)) && scope();
	
	Object around(TaintOnly m) : monitor(m) {
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
