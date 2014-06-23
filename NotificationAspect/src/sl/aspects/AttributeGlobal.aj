package sl.aspects;

public aspect AttributeGlobal extends AttributeBase {
	pointcut probe(): set(* *)  && scope() && if(enabled);
	
	static boolean enabled = false;
	public static void enable(boolean b) {
		enabled = b;
	}
}
