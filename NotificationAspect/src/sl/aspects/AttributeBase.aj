package sl.aspects;
import org.aspectj.lang.JoinPoint;

import sl.tools.Log;


public abstract aspect AttributeBase extends Base {
	pointcut probe();
	
	before() : probe() {
		JoinPoint a = thisJoinPoint;
		Log.log_attribute("Attribute changed", a);
	}
}
