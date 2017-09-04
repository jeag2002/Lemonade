package be.vsko.jss.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import be.vsko.jss.common.AbstractEntity;
import be.vsko.jss.common.EditorData;
import be.vsko.jss.common.FeedbackMessage;
import be.vsko.jss.common.Util;
import be.vsko.jss.exception.InvalidEntityException;

import com.google.gson.Gson;

/**
 * @author lionel
 *
 */
public class GeneralEditor extends BaseServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void serve(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");		
		String action = req.getParameter("action");
		String entity = req.getParameter("entity");
		
		if(action == null && id != null) {
			actionLoad(req, resp);
		}		
		else if((action == null || action.equals("new")) && id == null) {
			actionNew(req, resp);
		}
		else if(action != null && action.equals("save")) {
			//save the changes
			actionSave(req, resp);
		}
		else if(action != null && action.equals("delete")) {
			//delete the entity
			actionDelete(req, resp);
		}
		else {
			RequestDispatcher rd = getServletContext().getRequestDispatcher(getEditorURL(entity));		
			rd.forward(req, resp);
		}

	}

	protected void actionNew(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String entityName = req.getParameter("entity");				
			
		req.setAttribute("entity", Util.createAbstractEntity(entityName));
		
		RequestDispatcher rd = getServletContext().getRequestDispatcher(getEditorURL(entityName));		
		rd.forward(req, resp);
	}
	
	/**
	 * Deletes the entity. This is an ajax call
	 */
	@SuppressWarnings("unchecked")
	protected void actionDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		String entityName = req.getParameter("entity");
		AbstractEntity entity = null;
		String result = "";
		
		if(id != null && !id.isEmpty()) {
			Class clazz = Util.getEntityClass(entityName);							
			
			try {
				entity = AbstractEntity.findById(clazz, Integer.parseInt(id));
				entity.delete();
			}
			catch(InvalidEntityException e) {
				Map<String, FeedbackMessage> feedbackMap = new HashMap<String, FeedbackMessage>();
				int count=1;
				
				for (FeedbackMessage message : e.getValidationResult().getMessages()) {
					feedbackMap.put("feedback"+count, message);
					count++;
				}
				
				Gson gson = new Gson();
				result = gson.toJson(feedbackMap);
			}
			
		}
		resp.setContentType("application/json;charset=utf-8");
		resp.getWriter().write(result);
	}
	
		
	protected void actionSave(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		EditorData data = buildEditorData(req);
		String id = req.getParameter("id");
		String entityName = req.getParameter("entity");	
		
		doSave(req, resp, entityName, id, data, true);
	}
	
	@SuppressWarnings("unchecked")
	protected AbstractEntity doSave(HttpServletRequest req, HttpServletResponse resp, String entityName,  String id, EditorData data, boolean redirect) throws ServletException, IOException {		
		
		AbstractEntity entity = null;		
		
		try {
			if(id == null || id.isEmpty()) {
				//its an insert				
				entity = Util.createAbstractEntity(entityName);
				entity.set(data);
				entity.setCreatedBy("admin");
				entity.setModifiedBy("admin");
				entity.save();
				id = entity.getId().toString();
			}
			else {
				//its an update
				Class clazz = Util.getEntityClass(entityName);					
				entity = AbstractEntity.findById(clazz, Integer.parseInt(id));
				entity.set(data);
				entity.setModifiedBy("admin");
				entity.update();					
			}
			
			if(redirect) {
				if(req.getParameter("returnToEditor") != null && req.getParameter("returnToEditor").equals("1")) 
					resp.sendRedirect(resp.encodeRedirectURL("generalEditor?entity="+entityName+"&id="+id));						
				else if(req.getParameter("close") != null && req.getParameter("close").equals("1")) {
					RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/pages/closeColorbox.jsp");		
					rd.forward(req, resp);
				}
				else
					resp.sendRedirect(resp.encodeRedirectURL(getSelectorURL(entityName)));
			}
									
		}
		catch(InvalidEntityException e) {
			for (FeedbackMessage message : e.getValidationResult().getMessages()) {
				addFeedbackMessage(req, message);
			}
			
			req.setAttribute("entity", entity);
			
			if(redirect) {
				RequestDispatcher rd = getServletContext().getRequestDispatcher(getEditorURL(entityName));			
				rd.forward(req, resp);
			}
		}
		
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	protected EditorData buildEditorData(HttpServletRequest req) {
		EditorData editorData = new EditorData();
		
		Enumeration<String> en = req.getParameterNames();
		while(en.hasMoreElements()) {
			String name = en.nextElement();
			editorData.addAttribute(name, req.getParameter(name));
		}
		
		return editorData;
	}
	
	protected void actionLoad(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer id = Integer.parseInt(req.getParameter("id"));
		String entityName = req.getParameter("entity");				
		
		//get it by id and put it in the request
		req.setAttribute("entity", getEntity(entityName, id));					
											
		RequestDispatcher rd = getServletContext().getRequestDispatcher(getEditorURL(entityName));		
		rd.forward(req, resp);
	}
	
	@SuppressWarnings("unchecked")
	protected AbstractEntity getEntity(String entityName, Integer id) {
		Class clazz = Util.getEntityClass(entityName);
		
		return AbstractEntity.findById(clazz, id);
	}
	
	
	protected String getEditorURL(String entityName) {
		return "/WEB-INF/pages/"+entityName+"Editor.jsp";
	}
	
	protected String getSelectorURL(String entityName) {
		return "generalSelector?entity="+entityName;
	}
}
