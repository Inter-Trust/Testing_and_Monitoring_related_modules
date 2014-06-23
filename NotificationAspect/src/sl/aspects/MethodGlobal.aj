package sl.aspects;

public aspect MethodGlobal extends MethodBase {
	//pointcut methods(): execution(* *(..)) ; 
	pointcut monitor(): execution(* *(..))  && scope() && if(enabled);

	static boolean enabled = false;
	public static void enable(boolean b) {
		enabled = b;
	}
}
