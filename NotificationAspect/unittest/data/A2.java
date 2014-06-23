package data;
import sl.*;

public class A2 {

	public int ping()  {
		return 12;
	}
	
	public void outer() { inner();}

	@Taint
	public void taint() {inner();	}
	
	void inner() { innermost(); }
	
	@Exclude
	void innermost() {}
	
}
