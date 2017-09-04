package be.vsko.jss.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import be.vsko.jss.common.AbstractEntity;
import be.vsko.jss.common.IUser;
import be.vsko.jss.common.SelectorData;
import be.vsko.jss.common.Util;
import be.vsko.jss.common.jqg.HashMapJQGridRow;
import be.vsko.jss.common.jqg.JQGrid;
import be.vsko.jss.common.jqg.JQGridInfo;

/**
 * @author lionel
 *
 * Servlet used for the selectors. This Servlet expects few params:
 * action: 
 * 		"get" 	gets the list of entities that will be shown on the jqgrid. The get is invoked from the
 * 				jqgrid itself and expects an json response.
 * 			
 * 		"select" action called when the user double clicks an element of the grid, this will redirect him 
 * 				to the editor page.
 * 
 * 		empty value: will forward to the selector page
 * 
 * entity: the name of the entity that is being selected in singular and lower case, for example "user" or "person".
 */
public class GeneralSelector extends BaseServlet {

	private static final long serialVersionUID = 1L;
	
	private Logger log = Logger.getLogger(GeneralSelector.class);

	@Override
	protected void serve(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");
		String entity = req.getParameter("entity");
		
		if(action != null && action.equals("get")) {
			//gets the list of entities
			actionGet(req, resp);
		}
		else {
			RequestDispatcher rd = getServletContext().getRequestDispatcher(getSelectorURL(entity));		
			rd.forward(req, resp);
		}


	}
	
	/**
	 * This action is performed by the jqgrid to populate itself. Gets all the
	 * entities based on the filters and transform them into json.
	 */
	private void actionGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JQGridInfo jqinfo = new JQGridInfo(req);
		String entity = req.getParameter("entity");
		
		SelectorData selectorData = new SelectorData();
		selectorData.setGridInfo(jqinfo);
		selectorData.setUser((IUser)req.getSession().getAttribute("user"));
		setExtraFilters(selectorData, req);
		
		List<HashMap<String, Object>> rows = getSelectorList(entity, selectorData);
		Integer totalRows = getSelectorListCount(entity, selectorData);
		
		//this grid contains the data, it will be sent using json
		JQGrid grid = new JQGrid(jqinfo, totalRows);							
		for (HashMap<String, Object> row : rows) {						
			grid.getRows().add(new HashMapJQGridRow(row));
		}			
				
					
		resp.setContentType("application/json;charset=utf-8");
		resp.getWriter().write(grid.asJson());
	}
	
	/**
	 * Method to override. Adds parameters to the selector data which will be used to filter
	 * the rows for the grid.
	 */
	protected void setExtraFilters(SelectorData selectorData, HttpServletRequest req) {
		String val = req.getParameter("extraFilter");
		
		if(val != null)
			selectorData.getExtra().put("extraFilter", val);
	}
	
	/**
	 * Gets the URL where the editor is
	 */
	protected String getEditorURL(String entityName) {
		return "generalEditor?entity="+entityName;
	}
	
	/**
	 * Gets the URL where the selector is
	 */
	protected String getSelectorURL(String entityName) {
		return "/WEB-INF/pages/"+entityName+"Selector.jsp";
	}
	
	
	@SuppressWarnings("static-access")
	protected List<HashMap<String, Object>> getSelectorList(String entityName, SelectorData selectorData) {
		AbstractEntity entity = null;
		List<HashMap<String, Object>> result = null;
		entity = Util.createAbstractEntity(entityName);
			
		result = entity.findForSelector(entity.getClass(), selectorData);
		
		return result;
	}
	
	@SuppressWarnings("static-access")
	protected Integer getSelectorListCount(String entityName, SelectorData selectorData) {
		AbstractEntity entity = null;
		Integer result = null;
		entity = Util.createAbstractEntity(entityName);
			
		result = entity.getCountForSelector(entity.getClass(), selectorData);
		
		return result;
	}
}
