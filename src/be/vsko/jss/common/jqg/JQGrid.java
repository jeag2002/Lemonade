package be.vsko.jss.common.jqg;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class JQGrid {
	private int page;
	private int total;
	private int records;
	private List<JQGridRow> rows;
	
	public JQGrid(JQGridInfo jqinfo, Integer totalRows) {
		rows = new ArrayList<JQGridRow>();
		
		setPage(jqinfo.getPage());
		setRecords(totalRows);
		setTotal(jqinfo.getPageCount(totalRows));
	}
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getTotal() {
		return total;
	}
	
	public void setTotal(int total) {
		this.total = total;
	}
	
	public int getRecords() {
		return records;
	}
	
	public void setRecords(int records) {
		this.records = records;
	}
	
	public List<JQGridRow> getRows() {
		return rows;
	}
	
	public void setRows(List<JQGridRow> rows) {
		this.rows = rows;
	}

	public String asJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
}
