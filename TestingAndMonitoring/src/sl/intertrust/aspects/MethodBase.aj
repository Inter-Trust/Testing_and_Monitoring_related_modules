package sl.intertrust.aspects;

import sl.intertrust.MethodDuration;

abstract public aspect MethodBase extends Base {
	pointcut monitor();
	
	Object around() : monitor() {
		MethodDuration md = new MethodDuration(thisJoinPoint);
		Object return_value = null;
		boolean success = false;
		try{
			return_value = proceed();
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