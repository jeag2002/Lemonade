package be.vsko.jss.common;

import java.util.HashMap;
import java.util.Map;

import be.vsko.jss.common.jqg.JQGridInfo;

public class SelectorData {
	private JQGridInfo gridInfo;
	private IUser user;	
	private Map<String, Object> extra;
	
	public SelectorData() {
		extra = new HashMap<String, Object>();
	}

	public JQGridInfo getGridInfo() {
		return gridInfo;
	}

	public void setGridInfo(JQGridInfo gridInfo) {
		this.gridInfo = gridInfo;
	}

	public IUser getUser() {
		return user;
	}

	public void setUser(IUser user) {
		this.user = user;
	}

	public Map<String, Object> getExtra() {
		return extra;
	}

	public void setExtra(Map<String, Object> extra) {
		this.extra = extra;
	}

	
	
}
