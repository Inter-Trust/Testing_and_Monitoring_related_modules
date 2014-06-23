package sl.aspects;


public aspect CountGlobal extends CountBase {
	pointcut count_class(): initialization(*.new(..)) && scope() && if(enabled);
	
	static boolean enabled = false;
	public static void enable(boolean b) {
		enabled = b;
	}	
}
