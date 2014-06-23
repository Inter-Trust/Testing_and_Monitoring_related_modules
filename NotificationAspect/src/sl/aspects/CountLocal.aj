package sl.aspects;

import sl.Count;

public aspect CountLocal extends CountBase {
	pointcut count_class(): initialization(*.new(..)) && scope() && within(@Count *);
}
