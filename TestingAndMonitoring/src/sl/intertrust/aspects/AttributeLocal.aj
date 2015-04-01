package sl.intertrust.aspects;

import sl.intertrust.annotations.Monitor;

public aspect AttributeLocal extends AttributeBase {

	pointcut classes(): set(* *) && within(@Monitor *); // All attribute setters within @Monitor annotated class
	pointcut probe(): (set(@Monitor * *) || classes()) && scope();
}