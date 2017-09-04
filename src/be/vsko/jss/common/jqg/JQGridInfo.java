package be.vsko.jss.common.jqg;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

/**
 * @author lionel
 *
 * Helper class to parse the JQuery grid info from the request
 */
public class JQGridInfo {
	private Integer page;
	private String sortField;
	private Integer rows;
	private String sort;
	private Boolean search;
	private JQFilter filter;
	
	
	public JQGridInfo(HttpServletRequest req) {
		
		try {
			page = Integer.parseInt(req.getParameter("page"));		
		}
		catch(NumberFormatException ignore) {}
		
		try {
			rows = Integer.parseInt(req.getParameter("rows"));
		}
		catch(NumberFormatException ignore) {}
		
		sortField = req.getParameter("sidx");
		sort = req.getParameter("sord");
		
		search = (req.getParameter("_search") != null && req.getParameter("_search").equals("true"));
		if(search) {
			//get the search filters
			String filters = req.getParameter("filters");
			if(filters != null && !filters.isEmpty()) {
				Gson gson = new Gson();
				filter = gson.fromJson(filters, JQFilter.class);								
			}
		}
						
	}

	public Integer getPage() {
		return page;
	}

	public String getSortField() {
		return sortField;
	}
	
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public Integer getRows() {
		return rows;
	}

	public String getSort() {
		return sort;
	}
	
	public Integer getIndexFrom() {
		Integer from = 0; 
		
		if(page != null && rows != null)
			from = (page - 1) * rows;
		
		return from;
	}
	
	public Integer getIndexTo(Integer totalRows) {
		Integer to = 0; 
		
		if(page != null && rows != null)
			to = ((page - 1) * rows) + rows;
		
		if(to > totalRows)
			to = totalRows;
		
		return to;
	}
	
	public boolean isAsc() {
		return (sort == null || sort.equals("asc"));
	}
	
	public Integer getPageCount(Integer totalRows) {
		
		Double round = Math.ceil(new Double(totalRows) / new Double(rows));
		
		return round.intValue();
	}
	
	public JQFilter getFilter() {
		return filter;
	}

	public void setFilter(JQFilter filter) {
		this.filter = filter;
	}
	
	public Boolean getSearch() {
		return search;
	}

	
}
