package be.vsko.jss.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditorData {
	private Map<String, Object> attributes;

	public EditorData() {
		attributes = new HashMap<String, Object>();
	}
	
	public void addAttribute(String name, Object value) {
		attributes.put(name, value);
	}
	
	public void removeAttribute(String name) {
		attributes.remove(name);
	}
	
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	public Integer getIntegerAttribute(String name) {
		Integer result = null;
		
		if(attributes.get(name) != null) {
			try {
				if(!((String)attributes.get(name)).isEmpty())
					result = Integer.parseInt((String)attributes.get(name)); 
			}
			catch(NumberFormatException e) {
				result = null;
			}
		}
		
		return result;
	}
	
	public String getStringAttribute(String name) {
		return (String)attributes.get(name);
	}
	
	public Long getLongAttribute(String name) {
		Long result = null;
		
		if(attributes.get(name) != null) {
			try {
				if(!((String)attributes.get(name)).isEmpty())
					result = Long.parseLong((String)attributes.get(name)); 
			}
			catch(NumberFormatException e) {
				result = null;
			}
		}
		
		return result;
	}
	
	public Double getDoubleAttribute(String name) {
		Double result = null;
		
		if(attributes.get(name) != null) {
			try {
				if(!((String)attributes.get(name)).isEmpty())
					result = Double.parseDouble((String)attributes.get(name)); 
			}
			catch(NumberFormatException e) {
				result = null;
			}
		}
		
		return result;
	}
	
	public Date getDateAttribute(String name) {		
		//return DateUtil.sqlStringToDate((String)attributes.get(name));
		return DateUtil.stringToDate((String)attributes.get(name));
	}
	
	public Date getDateStrAttribute(String name) {		
		return DateUtil.stringToDate((String)attributes.get(name));
	}
	
	public Boolean getBooleanAttribute(String name) {
		Boolean result = false;
		Object val = attributes.get(name);
		if(val != null) {
			if(val.getClass().equals(Boolean.class))
				result = (Boolean)attributes.get(name);
			else if(val.getClass().equals(String.class)) {
				String str = (String)val;
				result = (str.toLowerCase().equals("true") || str.toLowerCase().equals("t") || str.equals("1"));
			}
		}
			
		return result;
	}
}
