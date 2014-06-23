package sl.tools;

import java.util.ArrayList;

import sl.Const;

// Output buffer used for unit testing.
public class LogBuffer {
	ArrayList<AttributeStore> logs = new ArrayList<AttributeStore>();

	public void log(AttributeStore as) {
		// as.push_front(Const.log_msg, msg);
		logs.add(as);
	}

	public void clear() { logs.clear(); }
	public Object size() { return logs.size();	}

	public AttributeStore Entry(int i) throws Exception {
		if(i>logs.size()) 
			throw new Exception(
					String.format("logs.size = %d < %d", logs.size(), i));
		return logs.get(i);
	}
}
