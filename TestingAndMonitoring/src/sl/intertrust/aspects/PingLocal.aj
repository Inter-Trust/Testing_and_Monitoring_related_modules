package sl.intertrust.aspects;

import sl.intertrust.annotations.Ping;

public aspect PingLocal extends PingBase {
	pointcut methods(): execution(@Ping * *(..)); // Methods with @Ping annotation
	pointcut all_in_class(): execution(* *(..)) && within(@Ping *);
	pointcut monitor(): (methods() || all_in_class()) && scope();
}
