package unittest;
import sl.Inject;
import sl.tools.Log;

@Inject(reset="reset_me_please", ready="is_ready")
public class A7 {
	public void reset_me_please() { Log.i("A7 reset"); }
	public Boolean is_ready() {
		Log.i("A7 ready");
		return true; 
	}
}
