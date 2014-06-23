package data;
import sl.*;

@Count
public class A5 {
	@Ping
	public void ping() {};
	
	@PingOnly(mask=Const.all ^ Const.name)
	public void pingonly() {};
}
