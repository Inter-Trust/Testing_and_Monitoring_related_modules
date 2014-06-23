package sl.aspects;

import sl.Exclude;

public abstract aspect Base {
	pointcut exclude_element(): execution(@Exclude * *(..)) || set(@Exclude * *); 
	pointcut exclude_class(): (set(* *) || execution(* *(..))) && within(@Exclude *); 
	pointcut exclude(): cflow(exclude_element() || exclude_class());
	protected pointcut scope(): !(	
			within(sl.*)   ||
			within(sl.*.*) || 
			within(sl.*.*.*) ||
			within(unittest.*) ||
			exclude());	// Eliminate aspects from operation scope
}
