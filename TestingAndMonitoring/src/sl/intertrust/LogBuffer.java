package sl.intertrust;

import java.util.ArrayList;

// This is the output buffer used for unit testing.
public class LogBuffer {
	ArrayList<AttributeStore> logs = new ArrayList<AttributeStore>();

	public void log(AttributeStore as) {
		logs.add(as);
	}

	public void clear() { logs.clear(); }
	public Object size() { return logs.size();	}

	public AttributeStore Entry(int i) throws Exception {
		if(i>=logs.size())
			throw new Exception(String.format("Entry with unexisting index requested: %d >= logs.size = %d", i, logs.size()));
		return logs.get(i);
	}
}
