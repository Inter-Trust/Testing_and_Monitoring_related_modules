package data;

import sl.intertrust.Log;
import sl.intertrust.annotations.Testing;

@Testing(reset="reset_me_please", ready="is_ready")
public class A7 {
	public void reset_me_please() { Log.notif_testinit("A7 reset"); }
	public Boolean is_ready() {
		Log.notif_testinit("A7 ready");
		return false; 
	}
}
