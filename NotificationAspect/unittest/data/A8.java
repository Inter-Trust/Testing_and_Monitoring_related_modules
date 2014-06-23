package data;
import sl.Exclude;

public class A8 {
	@Exclude
	int variableA;
	int variableB;

	public int ping()  { return 12;	}
	
	@Exclude
	public void outer() { inner();	}
	
	public void change_variables() { 
		inner();
		variableA = 1;
		variableB = 2;
	}
	
	void inner() {}
}