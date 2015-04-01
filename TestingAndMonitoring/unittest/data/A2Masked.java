package data;

import sl.intertrust.Const;
import sl.intertrust.annotations.Exclude;
import sl.intertrust.annotations.TaintOnly;

public class A2Masked {

	public int ping()  {
		return 12;
	}
	
	public void outer(String password) { inner(password);}

	@TaintOnly(mask=Const.all ^ Const.method_params)
	public void taint(String password) {inner(password);	}
	
	void inner(String password) { innermost(password); }
	
	@Exclude
	void innermost(String password) {}
	
}
