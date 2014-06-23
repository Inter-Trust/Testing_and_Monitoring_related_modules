package sl.aspects;

public aspect PingGlobal extends PingBase {
	declare precedence: PingGlobal, MethodGlobal;

	pointcut methods(): execution(* *(..)); 
	pointcut monitor(): methods() && scope() && if(enabled);
	
	static boolean enabled = false;
	public static void enable(boolean b) {
		enabled = b;
	}
}
