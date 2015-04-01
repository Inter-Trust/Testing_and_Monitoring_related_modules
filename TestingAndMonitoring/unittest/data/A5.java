package data;

import sl.intertrust.Const;
import sl.intertrust.annotations.Count;
import sl.intertrust.annotations.Ping;
import sl.intertrust.annotations.PingOnly;

@Count
public class A5 {
	@Ping
	public void ping() {};
	
	@PingOnly(mask=Const.all ^ Const.name)
	public void pingonly() {};
}
