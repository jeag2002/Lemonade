package be.lemonade.vanillitravels.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import be.vsko.jss.servlet.BaseServlet;

public class MainServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;
	
	protected void serve(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//go to the main page
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/pages/main.jsp");		
		rd.forward(req, resp);	
	}
	

}


