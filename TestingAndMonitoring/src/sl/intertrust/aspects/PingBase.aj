package sl.intertrust.aspects;

import sl.intertrust.Log;

public abstract aspect PingBase extends Base {
	pointcut monitor();
	
	before() : monitor() {
		Log.notif_ping(thisJoinPoint);
	}
}
