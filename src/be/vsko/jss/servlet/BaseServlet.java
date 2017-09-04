package be.vsko.jss.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import be.vsko.jss.common.Feedback;
import be.vsko.jss.common.FeedbackMessage;
import be.vsko.jss.common.JSSConfigurationProperties;


/**
 * @author lionel
 *
 * Base class for the servlets.
 */
public abstract class BaseServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public static final String ATTRIBUTE_MESSAGE = "message";
	public static final String ATTRIBUTE_VERSION = "version";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		try {
			beforeServe(req);
		
			serve(req, resp);
		
			afterServe(req);
		}		
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			beforeServe(req);
			
			serve(req, resp);
			
			afterServe(req);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
		

	
	protected void beforeServe(HttpServletRequest req) {		
		req.setAttribute(ATTRIBUTE_MESSAGE, "");		
		req.setAttribute(ATTRIBUTE_VERSION, JSSConfigurationProperties.getInstance().getVersion());				
	}
	
	protected void afterServe(HttpServletRequest req) {
		
	}
	
	protected abstract void serve(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;



	/**
	 * Adds a message that will be displayed in the feedback container
	 */
	protected void addFeedbackMessage(HttpServletRequest req, String message, String field) {			
		addFeedbackMessage(req, new FeedbackMessage(message, field));
	}
	
	protected void addFeedbackMessage(HttpServletRequest req, String message) {
		addFeedbackMessage(req, new FeedbackMessage(message, ""));
	}
	
	protected void addFeedbackMessage(HttpServletRequest req, FeedbackMessage feedbackMessage) {
		Feedback feedback = (Feedback)req.getAttribute("feedback");
		
		if(feedback == null) {
			feedback = new Feedback();
			req.setAttribute("feedback", feedback);
		}
		
		feedback.addMessage(feedbackMessage);
	}
}
