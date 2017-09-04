package be.vsko.jss.common;

/**
 * @author lionel
 *
 * Simple class that represents one option to be displayed in an autocomplete or a combo.
 */
public class Option {
	private Object value;
	private String label;
	
	public Option(Object value, String label) {
			this.value = value;
			this.label = label;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	
}
