package data;
import sl.*;
import static sl.Const.*;

public class MaskTest {
	@Monitor
	int variableA;
	@MonitorOnly(mask=all ^ source_line ^ attribute_value)
	int variableB;
	
	@Ping
	public int ping()  { return 12;	}

	@MonitorOnly(mask=all ^ method_params ^ source_line)
	public void outer() throws Exception { inner();	}
	
	public void change_variables() throws Exception { 
		inner2("hello", 14);
		variableA = 1;
		variableB = 2;
	}
	
	void inner() { }

	@Monitor
	void inner2(String a, long l) {	}

	@MonitorOnly(mask = log_msg | name)
	public int params(String str, int val)  { return 12; }

}
