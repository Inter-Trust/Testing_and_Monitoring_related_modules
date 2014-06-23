package sl.aspects;

import sl.Monitor;

public aspect MethodLocal extends MethodBase {
	pointcut methods(): execution(@Monitor * *(..)); // Methods with @Monitor annotation
	pointcut classes(): execution(* *(..)) && within(@Monitor *); //All methods within annotated class
	pointcut monitor(): (methods() || classes()) && scope();
}
