package sl.aspects;
import sl.tools.Log;

public abstract aspect PingBase extends Base {
	pointcut monitor();
	
	before() : monitor() {
		Log.i("Ping", thisJoinPoint);
	}
}
