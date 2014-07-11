package sl.aspects;

import org.aspectj.lang.JoinPoint;

//import sl.Demon;
import sl.Inject;
import sl.tools.*;

public aspect InjectAspect  {
//	static Demon demon;
	
	pointcut injected_class(): initialization(*.new(..)) &&  within(@Inject *);
	
	before() : injected_class() {
		JoinPoint jp = thisJoinPoint;
		Injector inj = Injector.singleton();
		// Inner classes may cause a false positive.
		// We have to check for the annotation to filter that out.
		if( inj.hasAnnotation(jp.getTarget())) { 
			inj.add_reset_function(jp.getTarget());
			inj.add_ready_function(jp.getTarget());
			Log.i("Inject acquired object", thisJoinPoint);
		}
	}
	
//	public static void start(int port) {
//		demon = new Demon(port);
//	}
}
