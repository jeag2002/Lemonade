package be.vsko.jss.servlet;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import be.vsko.jss.common.AbstractEntity;
import be.vsko.jss.common.EntitySelector;
import be.vsko.jss.common.SelectorData;
import be.vsko.jss.common.jqg.HashMapJQGridRow;
import be.vsko.jss.common.jqg.JQFilter;
import be.vsko.jss.common.jqg.JQGrid;
import be.vsko.jss.common.jqg.JQGridInfo;
import be.vsko.jss.common.jqg.JQRule;
import be.vsko.jss.exception.JSSException;

public class ServletUtils {

	public static String getJsonForJQGrid(HttpServletRequest req, AbstractEntity entity, EntitySelector entitySelector) {
		String filterColumn = req.getParameter("filterColumn");
		String filterValue = req.getParameter("filterValue");
		String json = "";
		
		try {
			SelectorData selectorData = new SelectorData();
			JQGridInfo info = new JQGridInfo(req);
			JQRule rule = new JQRule();
			selectorData.setGridInfo(info);
			
			//create the rule for filtering
			if(filterColumn != null && filterValue != null) {
				rule.setField(filterColumn);
				rule.setOp("eq");
				rule.setData(filterValue);
				
				
				//if there's already a filter, add it there, otherwise create one
				if(info.getFilter() != null)
					info.getFilter().getRules().add(rule);
				else {
					JQFilter filter = new JQFilter();
					filter.setGroupOp("and");
					filter.getRules().add(rule);
					info.setFilter(filter);
				}
			}
									
			//List<HashMap<String, Object>> rows = entity.findForSelector(entity.getClass(), selectorData);
			List<HashMap<String, Object>> rows = entitySelector.getRows(entity.getClass(), selectorData);
			int totalRows = entitySelector.getTotalRows(entity.getClass(), selectorData);
			
			//take a sub set for pagination
//			rows = rows.subList(info.getIndexFrom(), info.getIndexTo(totalRows));
			
			//this grid contains the data, it will be sent using json
			JQGrid grid = new JQGrid(info, totalRows);					
			for (HashMap<String, Object> row : rows) {							
				grid.getRows().add(new HashMapJQGridRow(row));
			}			
											
			json = grid.asJson();
		}
		catch(Exception e) {
			throw new JSSException(e);
		}
		
		return json;
	}
}
