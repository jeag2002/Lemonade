package be.vsko.jss.common;

import java.util.HashMap;
import java.util.List;

public abstract class EntitySelector {
	
	public EntitySelector() {
	}
	
	@SuppressWarnings("rawtypes")
	public abstract List<HashMap<String, Object>> getRows(Class clazz, SelectorData selectorData);
	
	@SuppressWarnings("rawtypes")
	public abstract Integer getTotalRows(Class clazz, SelectorData selectorData);
}
