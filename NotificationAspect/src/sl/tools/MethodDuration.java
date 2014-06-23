package sl.tools;


import java.util.Locale;

import org.aspectj.lang.JoinPoint;

import sl.Const;

public class MethodDuration {
	long start;
	String ex = null;
	long mask;

	public MethodDuration(JoinPoint jp) {
		init(jp, Const.all);
	}
	
	public MethodDuration(JoinPoint jp, long log_mask) {
		init(jp, log_mask);
	}
	
	private void init(JoinPoint jp, long log_mask) {
		mask = log_mask;
		start = System.nanoTime(); 
		AttributeStore as = Log.getAttributeStore(mask);
		Log.get_parameters(jp, as);
		Log.i("Method Enter", jp, as);		
	}

	public void set_exception(Exception e) {
		ex = "\""+e.toString()+"\"";
	}
	

	public void on_end(JoinPoint jp, boolean success, Object return_value) {
		long end = System.nanoTime();
		AttributeStore as = Log.getAttributeStore(mask);
		boolean no_ex = ex == null;
		as.push_back(Const.exit_status, no_ex?"success":"exception_thrown");
		if(!no_ex) as.push_back(Const.exception, ex);
		String duration = String.format(Locale.ENGLISH,"%.3f", (end-start)/1000.0);
		as.push_back(Const.duration, duration);
		if(success)
			as.push_back(Const.return_value, return_value);
		Log.i("Method Leave", jp, as);		
	}
}
