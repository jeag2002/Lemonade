package be.vsko.jss.common.jqg;

import java.util.HashMap;


public class HashMapJQGridRow extends JQGridRow{

	public HashMapJQGridRow(HashMap<String, Object> map) {
		setId((Integer)map.get("id"));
		
		setCell(map.values().toArray());
				
	}
}
