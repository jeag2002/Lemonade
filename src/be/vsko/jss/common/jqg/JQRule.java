package be.vsko.jss.common.jqg;

/**
 * @author lionel
 *
 * Helper class for filtering rules of JQuery Grid
 */
public class JQRule {
	private String field;
	private String op;
	private String data;
	
	public JQRule() {
		
	}
	
	public JQRule(String field, String op, String data) {
		this.field = field;
		this.op = op;
		this.data = data;
	}
	
	public String getField() {
		return field;
	}
	
	public void setField(String field) {
		this.field = field;
	}
	
	public String getOp() {
		return op;
	}
	
	public void setOp(String op) {
		this.op = op;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	public String getAsSQL(Class clazz, AttributeSqlHandler ash) {		
		
		return ash.getSqlFilter(clazz, field, data, op);
	}

	
	
}
