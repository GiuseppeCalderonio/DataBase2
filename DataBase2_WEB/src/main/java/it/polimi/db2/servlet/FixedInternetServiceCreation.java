package it.polimi.db2.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.db2.Creation;
import it.polimi.db2.HTMLhelper.HTMLPrinter;
import it.polimi.db2.jee.stateless.EmployeeManager;
import it.polimi.db2.jee.stateless.UserManager;

/**
 * Servlet implementation class InternetServiceCreation
 */
@WebServlet("/FixedInternetServiceCreation")
public class FixedInternetServiceCreation extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name = "it.polimi.db2.jee.stateless/UserManager")
	private UserManager userManager;
	
	@EJB(name = "it.polimi.db2.jee.stateless/EmployeeManager")
	private EmployeeManager employeeManager;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FixedInternetServiceCreation() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = getServletContext().getContextPath() + "/LoginServlet";
		response.sendRedirect(path);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int userId;
		String username;
		
		try {
			
			// get the user id associated with the user of this session IF exists
			
			userId = (int) request.getSession().getAttribute("userId");
			
			// get the username associated with the user of this session IF exists
			
			username = userManager.findById(userId).getUsername();
			
			}catch(NullPointerException e) {
			
			// the user didn't login
			
			username = null;
			
			// redirect the user to the login page (if not logged, he cannot access this page)
			// and finish the execution of the get
			
			String path = getServletContext().getContextPath() + "/LoginServlet";
			response.sendRedirect(path);
			return;
		}
		
		int GB;
		int GBfee;
		
		try { // to get the parameters
			
			// get the giga bytes
			
			GB = Integer.parseInt( request.getParameter("GIGA"));
			
			// get the giga bytes fee
			
			GBfee = Integer.parseInt(request.getParameter("feeExtraGB"));
			
			// create the service
			
			employeeManager.addFixedInternetService(GB, GBfee);
			
			// print the page
			
			printPage(response.getWriter(), "Service correctly created", username);
			
			
		}catch (NumberFormatException | NullPointerException | PersistenceException   e) {
			// somethong went wrong
			
			printPage(response.getWriter(), e.getMessage(), username);
		}
	}
	
	private void printPage(PrintWriter out, String errorMessage, String username) {
		new HTMLPrinter(out, "EmployeeHomePage").
		printEmployeeHomePage(errorMessage,
				username,
				Creation.FIXEDINTERNET,
				null,null, null, null, null);
	}

}
