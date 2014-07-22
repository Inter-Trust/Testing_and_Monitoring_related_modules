package data;
import sl.*;

public class A1 {
	@Monitor
	int variableA;
	int variableB;
	
	@Ping
	public int ping()  { return 12;	}
	
	@MonitorOnly(mask=Const.all ^ Const.method_params)
	public int params(String str, int val)  { return 12; }
	
	@Monitor
	public int params2(String str, int val)  { return 12; }
	
	@Monitor
	public void outer() throws Exception { inner();	}
	
	public void change_variables() throws Exception { 
		inner2();
		variableA = 1;
		variableB = 2;
	}
	
	void inner() throws Exception {
		throw new Exception("test exception");
	}

	void inner2() {	}
}
