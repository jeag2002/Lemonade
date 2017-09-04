package be.vsko.jss.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import be.vsko.jss.common.AbstractEntity;
import be.vsko.jss.common.EntitySelector;
import be.vsko.jss.common.Option;
import be.vsko.jss.common.SelectorData;
import be.vsko.jss.common.Util;
import be.vsko.jss.exception.JSSException;

import com.google.gson.Gson;


/**
 * @author lionel
 *
 * Servlet used for ajax requests
 */
public class GeneralAjaxServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void serve(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");
			
		if(action.equals("getEntityForAutocomplete")) {
			getEntityForAutocomplete(req, resp);
		}
		else if(action.equals("getValueForAutocompleteSelection")) {
			getValueForAutocompleteSelection(req, resp);
		}
		else if(action.equals("getForGrid")) {
			getForGrid(req, resp);
		}	
		
		//31-08-2017 JALCARAZ (ISSUE 5)
		else if(action.equals("calcTotCost")){
			calTotCost(req,resp);
		}
	}
	
	//31-08-2017 JALCARAZ (ISSUE 5)
	////////////////////////////////////////////////////
	
	@SuppressWarnings("static-access")
	protected void calTotCost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String result = "0";
		
		try{
			
			SimpleDateFormat sDF = new SimpleDateFormat("dd/MM/yyyy");
			
			String dateTo = req.getParameter("dateTo");
			String dateFrom = req.getParameter("dateFrom");
			
			Date dateToDate = sDF.parse(dateTo); 
			Date dateFromDate = sDF.parse(dateFrom);
			
			long numDays = 0;
			
			//num of days of the travel.
			if ((dateToDate.before(dateFromDate)) || (dateToDate.equals(dateFromDate))){
				numDays = 0;
			}else{
				numDays = dateToDate.getTime() - dateFromDate.getTime();
				numDays = numDays/(24 * 60 * 60 * 1000);
			}
			
			//hotel cost
			String hotelCostDaily = req.getParameter("hotelCostDaily");
			if (hotelCostDaily == null){
				hotelCostDaily = "0";
			}else if (hotelCostDaily.trim().equalsIgnoreCase("")){
				hotelCostDaily = "0";
			}
			Double hCDDouble = Double.parseDouble(hotelCostDaily);
			BigDecimal bDhCDDouble = new BigDecimal(hCDDouble);
			
			//journey cost
			String travelCost = req.getParameter("travelCost");
			if (travelCost == null){
				travelCost = "0";
			}else if (travelCost.trim().equalsIgnoreCase("")){
				travelCost = "0";
			}
			Double tCDouble = Double.parseDouble(travelCost);
			BigDecimal bDtCDouble = new BigDecimal(tCDouble); 
			
			
			//excursion cost
			String sigCost = req.getParameter("sigCost");
			if (sigCost == null){
				sigCost = "0";
			}else if (sigCost.trim().equalsIgnoreCase("")){
				sigCost = "0";
			}
			Double sigCostDouble = Double.parseDouble(sigCost);
			BigDecimal bDsigCostDouble = new BigDecimal(sigCostDouble);
			
			BigDecimal res = bDsigCostDouble.add(bDtCDouble);
			res = res.add(bDhCDDouble.multiply(new BigDecimal(numDays)));
			res = res.setScale(2, RoundingMode.CEILING);
			result = res.toPlainString();
			
		}catch(Exception e){
			result = "0";
		}
		
		
		resp.setContentType("text/plain");
		resp.getWriter().write(result);
	}
	
	///////////////////////////////////////////////////

		
	@SuppressWarnings("static-access")
	protected void getEntityForAutocomplete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String entityName = req.getParameter("entity");
		String term = req.getParameter("term");
		AbstractEntity entity = null;		
		List<Option> options = null;
				
		
		try {		
			entity = Util.createAbstractEntity(entityName);			
			options = entity.getAsAutocompleteOptions(entity.getClass(), term);
			
			Gson gson = new Gson();				
			resp.setContentType("application/json;charset=utf-8");
			
			resp.getWriter().write(gson.toJson(options));
		}
		catch(Exception e) {
			throw new JSSException(e);
		}		
		
	}
	
	@SuppressWarnings("static-access")
	protected void getValueForAutocompleteSelection(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String entityName = req.getParameter("entity");
		Integer id = null;
		AbstractEntity entity = null;
		String result = "";
		
		try {
			id = Integer.parseInt(req.getParameter("id"));
			entity = Util.createAbstractEntity(entityName);			
			result = entity.getAutocompleteOptionValue(entity.getClass(), id);													
		}
		catch(NumberFormatException e) {
			//do nothing
		}												
		catch(Exception e) {
			throw new JSSException(e);
		}		
		
		resp.setContentType("text/plain");
		resp.getWriter().write(result);
	}
	
	
	protected void getForGrid(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String entityName = req.getParameter("entity");		
		
		try {
			final AbstractEntity entity = Util.createAbstractEntity(entityName);
			
			String json = ServletUtils.getJsonForJQGrid(req, entity, new EntitySelector() {
				@SuppressWarnings({ "unchecked", "static-access" })
				public List<HashMap<String, Object>> getRows(Class clazz, SelectorData selectorData) {
					return entity.findForSelector(clazz, selectorData);
				}
				
				@SuppressWarnings({ "unchecked", "static-access" })
				@Override
				public Integer getTotalRows(Class clazz, SelectorData selectorData) {
					return entity.getCountForSelector(clazz, selectorData);
				}
			});
											
			resp.setContentType("application/json;charset=utf-8");
			resp.getWriter().write(json);
		}
		catch(Exception e) {
			throw new JSSException(e);
		}						
	}
	
	
	
	
}
