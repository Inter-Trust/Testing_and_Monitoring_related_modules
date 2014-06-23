package data;
import sl.*;

@Monitor
public class A3 {
	
	int variableA;
	@Exclude
	int variableB;
	
	public int ping()  { return 12;	}
	public void outer() { inner();	}
	public void change_variables() { 
		inner();
		variableA = 1;
		variableB = 2;
	}
	@Exclude
	void inner() {}
}
