package sl.aspects;
import sl.PingOnly;
import sl.tools.AttributeStore;
import sl.tools.Log;

public aspect PingLocalOnly extends Base {
	pointcut methods(PingOnly m): execution(@PingOnly * *(..)) && @annotation(m); // Methods with @Ping annotation
	pointcut all_in_class(PingOnly m): execution(* *(..)) && within(@PingOnly *) && @annotation(m);
	pointcut ping(PingOnly m): (methods(m) || all_in_class(m)) && scope();
	
	before(PingOnly m) : ping(m) {
		AttributeStore as = Log.getAttributeStore(m.mask());
		Log.i("Ping", thisJoinPoint, as);
	}
}
