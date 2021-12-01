package it.polimi.db2.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.persistence.NonUniqueResultException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringEscapeUtils;

import it.polimi.db2.HTMLhelper.Form;
import it.polimi.db2.HTMLhelper.FormInstance;
import it.polimi.db2.HTMLhelper.HTMLPrinter;
import it.polimi.db2.exceptions.CredentialsException;
import it.polimi.db2.jee.stateless.UserManager;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	
	@EJB(name = "it.polimi.db2.jee.stateless/UserManager")
	private UserManager userManager;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//response.sendRedirect(getServletContext().getContextPath() + "/LoginPage.html");
		printPage("", response.getWriter());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String usrn = null;
		String pwd = null;
		try {
			usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
			pwd = StringEscapeUtils.escapeJava(request.getParameter("pwd"));
			if (usrn == null || pwd == null || usrn.isEmpty() || pwd.isEmpty()) {
				throw new Exception("Missing or empty credential value");
			}

		} catch (Exception e) {
			// for debugging only e.printStackTrace();
			printPage(e.getMessage(), response.getWriter());
			return;
		}
		Integer userId = null;
		try {
			// query db to authenticate for user
			userId = userManager.checkCredentials(usrn, pwd);
		} catch (CredentialsException | NonUniqueResultException e) {
			e.printStackTrace();
			printPage(e.getMessage(), response.getWriter());
			//response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not check credentials");
			return;
		}

		// If the user exists, add info to the session and go to home page, otherwise
		// show login page with error message

		String path;
		if (userId == null) {
			
			printPage("The login failed, be sure to have insert the right username and password", response.getWriter());
			
			
		} else if(!userManager.findById(userId).isEmployee()){
			
			request.getSession().setAttribute("userId", userId);
			path = getServletContext().getContextPath() + "/GoToHomePage";
			response.sendRedirect(path);
			
		}
		else {
			// send to employee home page
		}
	}
	
	private void printPage(String errorMessage, @NotNull PrintWriter out) {
		
		//response.sendRedirect(getServletContext().getContextPath() + "/LoginPage.html");
		
		new HTMLPrinter(out, "LandingPage").printLoginPage(errorMessage, "");
	}
	
	
	
	
	
	
	

}
