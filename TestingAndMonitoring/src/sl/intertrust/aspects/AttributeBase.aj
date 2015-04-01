package sl.intertrust.aspects;

import sl.intertrust.AttributeStore;
import sl.intertrust.Log;

public abstract aspect AttributeBase extends Base {
	pointcut probe();
	
	before() : probe() {
		AttributeStore as = Log.getAttributeStore(Log.getLogMask());
		Log.notif_attribute("Attribute changed", thisJoinPoint, as);
	}
}
