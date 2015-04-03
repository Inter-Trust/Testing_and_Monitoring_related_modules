package sl.intertrust.aspects;

import org.aspectj.lang.JoinPoint;

import sl.intertrust.Counter;

public abstract aspect CountBase extends Base {
	pointcut count_class();
	
	before() : count_class() {
		JoinPoint jp = thisJoinPoint;
		Counter.singleton().add(jp.getTarget(), jp.getTarget().getClass().getName());
	}
}
