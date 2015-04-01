package sl.intertrust.aspects;

import sl.intertrust.AttributeStore;
import sl.intertrust.Log;
import sl.intertrust.annotations.PingOnly;

public aspect PingLocalOnly extends Base {
	pointcut methods(PingOnly m): execution(@PingOnly * *(..)) && @annotation(m); // Methods with @Ping annotation
	pointcut all_in_class(PingOnly m): execution(* *(..)) && within(@PingOnly *) && @annotation(m);
	pointcut ping(PingOnly m): (methods(m) || all_in_class(m)) && scope();
	
	before(PingOnly m) : ping(m) {
		AttributeStore as = Log.getAttributeStore(m.mask());
		Log.notif_ping(thisJoinPoint, as);
	}
}
