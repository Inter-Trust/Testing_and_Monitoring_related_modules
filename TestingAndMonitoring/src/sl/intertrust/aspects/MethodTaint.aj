package sl.intertrust.aspects;

import sl.intertrust.annotations.Taint;

public aspect MethodTaint extends MethodBase {
	pointcut methods(): execution(@Taint * *(..)); 	// Methods with @Taint
	pointcut monitor() : cflow(methods()) && execution(* *(..)) && scope(); // @Taint's child methods
}
