package sl.aspects;
import org.aspectj.lang.JoinPoint;

import sl.tools.AttributeStore;
import sl.tools.Log;


public abstract aspect AttributeBase extends Base {
	pointcut probe();
	
	before() : probe() {
		JoinPoint a = thisJoinPoint;
		AttributeStore as = Log.getAttributeStore(Log.getLogMask());
		Log.log_attribute("Attribute changed", thisJoinPoint, as);
	}
}
