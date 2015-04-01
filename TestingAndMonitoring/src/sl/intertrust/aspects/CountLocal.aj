package sl.intertrust.aspects;

import sl.intertrust.annotations.Count;

public aspect CountLocal extends CountBase {
	pointcut count_class(): initialization(*.new(..)) && scope() && within(@Count *);
}
