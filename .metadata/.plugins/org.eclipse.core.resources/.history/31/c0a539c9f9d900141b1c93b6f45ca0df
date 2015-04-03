package sl.intertrust;

import java.util.ArrayList;


import com.montimage.mmt.client.connector.GenericFieldValueHeader;
import com.montimage.mmt.client.connector.MMTFieldValueHeader;

public class AttributeStore {
	ArrayList<Attribute> attributes = new ArrayList<Attribute>();
	boolean fail = false;
	long mask = Const.all;
	
	public void setLogMask(long log_mask) {
		mask = log_mask;
	}
	
	public void push_back(long name, Object value) {
		if((name & mask) == 0) return;
		String n = Const.toStr(name);
		attributes.add(new Attribute(n, value==null?"null":value));
	}
	
	public void push_front(long name, Object value) {
		if((name & mask) == 0) return;
		attributes.add(0, new Attribute(Const.toStr(name), value));
	}
	
	public String toString() {
		if(attributes.isEmpty()) return "";
		StringBuffer buf = new StringBuffer();
		Attribute last = attributes.get(attributes.size()-1);
		for(Attribute a: attributes) {
			buf.append(a.name + ":"+a.value);
			if(a != last) buf.append(", ");
		}
		return buf.toString();
	}
	
	static public void add(ArrayList<MMTFieldValueHeader> ar, String key, String value) {
	   ar.add(new GenericFieldValueHeader(key, value));
	}

	public ArrayList<MMTFieldValueHeader> to_remote_log() {
		ArrayList<MMTFieldValueHeader> ar = new ArrayList<MMTFieldValueHeader>();
		for(Attribute a: attributes) add(ar, a.name, a.value);
		return ar; 
	}
	
	// unit test support
	public AttributeStore Has(String pair) throws Exception {
		String[] buf = pair.split(":");
		if(buf.length < 2) throw new Exception("'name:value' pair expected, got instead: "+pair);
		String head = buf[0];
		String tail = buf[1];
		if(buf.length > 2){
			StringBuilder sb = new StringBuilder(tail);
			for(int i=2; i<buf.length; i++) {
				sb.append(':');
				sb.append(buf[i]);
			}
			tail = sb.toString();
		}
		Has(head, tail);
		return this;
	}
	
	private AttributeStore Has(String name, String value) throws Exception {
		Attribute a = new Attribute(name, value);
		for(Attribute i: attributes)
			if(i.equals(a)) return this;
		throw new Exception(name+":"+value+" not found");
	}
}
