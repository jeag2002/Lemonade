package be.vsko.jss.common.jqg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author lionel
 *
 * Helper class for filtering a JQuery Grid
 */
public class JQFilter {
	private String groupOp;
	private List<JQRule> rules = new ArrayList<JQRule>();

	
	public String getGroupOp() {
		return groupOp;
	}
	
	public void setGroupOp(String groupOp) {
		this.groupOp = groupOp;
	}
	
	public List<JQRule> getRules() {
		return rules;
	}
	
	public void setRules(List<JQRule> rules) {
		this.rules = rules;
	}
	
	public JQRule getRule(String field) {
		JQRule result = null;
		
		for (JQRule rule : rules) {
			if(rule.getField().equals(field))
				result = rule;
		}
		
		return result;
	}
	
	public void removeRule(String field) {
		removeRule(getRule(field));
	}
	
	public void removeRule(JQRule rule) {
		if(rule != null)
			rules.remove(rule);
	}
	
	public boolean hasRule(String field) {
		Boolean found = false;
		
		Iterator<JQRule> it = rules.iterator();
		while(!found && it.hasNext()) {
			JQRule rule = it.next();
			found = (rule.getField().equals(field));
		}
		
		return found;
	}
	
	public boolean hasRules() {
		return !rules.isEmpty();
	}
	
	/**
	 * Gets a rule as SQL.
	 * @param ruleField The name of the field in the jqgrid.
	 * @param clazz The class this field references to.
	 * @param sqlField The name of the field in the sql select query, with the prefix.
	 * @return the where sql clause for this filter
	 */
	@SuppressWarnings("unchecked")
	public String getRuleAsSql(String ruleField, Class clazz, String sqlField) {
		JQRule rule = getRule(ruleField);
		String result = "";
		if(rule != null) 
			result = AttributeSqlHandler.getSqlFilter(clazz, sqlField, rule.getData(), rule.getOp()) + " ";
		
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public String getAsSql(Class clazz, AttributeSqlHandler attributeTypeHandler) {
		String result = "";
		
		Iterator<JQRule> it = rules.iterator();
		while(it.hasNext()) {
			JQRule rule = it.next();
			
			result += rule.getAsSQL(clazz, attributeTypeHandler);
			
			if(it.hasNext())
				result += " " + groupOp + " ";
		}
		
		return result;
	}
	
		
}
