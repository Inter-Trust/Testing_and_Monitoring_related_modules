package sl.intertrust.aspects;

import org.aspectj.lang.JoinPoint;
import sl.intertrust.Log;
import sl.intertrust.TestInit;
import sl.intertrust.annotations.Testing;

public aspect TestInitAspect  {

	pointcut injected_class(): initialization(*.new(..)) &&  within(@Testing *);
	
	before() : injected_class() {
		JoinPoint jp = thisJoinPoint;
		TestInit inj = TestInit.singleton();
		// Inner classes may cause a false positive.
		// We have to check for the annotation to filter that out.
		if( inj.hasAnnotation(jp.getTarget())) { 
			inj.add_reset_function(jp.getTarget());
			inj.add_ready_function(jp.getTarget());
			Log.notif_testinit("Test Init acquired object", thisJoinPoint);
		}
	}
}
