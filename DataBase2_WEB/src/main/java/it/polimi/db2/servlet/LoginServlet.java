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
		
		// delete if exists the attribute relative to the user login
		
		request.getSession().removeAttribute("userId");
		
		// print the login standard page
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
			
			// get the username chosen by the user
			
			usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
			
			// get the password chosen by the user
			
			pwd = StringEscapeUtils.escapeJava(request.getParameter("pwd"));
			if (usrn == null || pwd == null || usrn.isEmpty() || pwd.isEmpty()) {
				
				// if the data sent are inherently wrong
				
				throw new Exception("Missing or empty credential value");
			}

		} catch (Exception e) {
			
			// print the page again showing the error
			
			printPage(e.getMessage(), response.getWriter());
			return;
		}
		
		// declare a variable for the userId
		
		Integer userId = null;
		
		
		try {
			
			// query the database to find the id associated with the user given the credentils
			
			userId = userManager.checkCredentials(usrn, pwd);
			
		} catch (CredentialsException | NonUniqueResultException e) {
			
			
			e.printStackTrace();
			
			// print the page again showing the eventual error
			
			printPage(e.getMessage(), response.getWriter());
			
			return;
		}

		// If the user exists, add info to the session and go to home page, otherwise
		// show login page with error message

		String path;
		
		// if the database hasn't found any user associated with the credentials
		if (userId == null) {
			
			// print the page again showing the wrong credentials error
			
			printPage("The login failed, be sure to have insert the right username and password", response.getWriter());
			
		
		} else if(userManager.findById(userId).isEmployee()){ // if the user is an employee
			// send to employee home page
			
			// store the id of the user in the session attributes
			request.getSession().setAttribute("userId", userId);
			
			// send the employee to the home page of the employee application
			path = getServletContext().getContextPath() + "/GoToEmployeeHomePage";
			response.sendRedirect(path);
			
		}
		else { // else if the user found is not an employee
			
			// store the id of the user in the session attributes
			
			request.getSession().setAttribute("userId", userId);
			
			// get the next page name
			
			String nextPage = getNewPageName(request);
						
			// send the user to the next page according to the web login
						
			path = getServletContext().getContextPath() + nextPage;
			response.sendRedirect(path);
			
			
		}
	}
	
	private void printPage(String errorMessage, @NotNull PrintWriter out) {
		
		//response.sendRedirect(getServletContext().getContextPath() + "/LoginPage.html");
		
		new HTMLPrinter(out, "LandingPage").printLoginPage(errorMessage, "");
	}
	
	private String getNewPageName(HttpServletRequest request) {
		
		Object confirmationFlag = request.getSession().getAttribute("confirmationFlag");
		
		// if the request came from the confirmation page
		
		if(confirmationFlag != null && (boolean)confirmationFlag)
			return "/GoToConfirmationPage";
		
		// else go default in the home page
		
		return "/GoToHomePage";
		
	}

}
