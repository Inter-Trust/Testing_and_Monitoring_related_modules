package sl.aspects;

import sl.TaintMask;
import sl.tools.MethodDuration;

public aspect MethodTaintMask extends Base {
	pointcut methods(TaintMask m): execution(@TaintMask * *(..)) && @annotation(m); // Methods with @Monitor annotation
	pointcut monitor(TaintMask m): cflow(methods(m)) && execution(* *(..)) && scope();
	
	
	Object around(TaintMask m) : monitor(m) {
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
