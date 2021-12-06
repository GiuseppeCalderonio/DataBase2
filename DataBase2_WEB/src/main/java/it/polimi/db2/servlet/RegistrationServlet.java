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
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringEscapeUtils;

import it.polimi.db2.HTMLhelper.HTMLPrinter;
import it.polimi.db2.exceptions.CredentialsException;
import it.polimi.db2.jee.stateless.UserManager;

/**
 * Servlet implementation class RegistrationServet
 */
@WebServlet("/RegistrationServlet")
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name = "it.polimi.db2.jee.stateless/UserManager")
	private UserManager userManager;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistrationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//String path = getServletContext().getContextPath() + "/RegistrationPage.html";
		//response.sendRedirect(path);
		printPage("", response.getWriter());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String usrn = null;
		String pwd = null;
		String email = null;
		boolean isEmployee;
		try {
			// get the username chosen by the user
			
			usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
			
			// get the password chosen by the user
			
			pwd = StringEscapeUtils.escapeJava(request.getParameter("password"));
			
			// get the email chosen by the user
			
			email = StringEscapeUtils.escapeJava(request.getParameter("email"));
			
			// get the isEmployee flag chosen by the user
			
			String temp = (request.getParameter("isEmployee"));
			
			isEmployee = (temp != null);
			
			// if the data sent are inherently wrong
			
			if (usrn == null || pwd == null || email == null ||
					usrn.isEmpty() || pwd.isEmpty() || !email.contains("@")){
				throw new Exception("Missing or empty credential value");
			}
			
		}catch(Exception e) {
			
			// an error occured
			
			e.printStackTrace();
			printPage(e.getMessage(), response.getWriter());
			return;
		}
		try {
			
			// try to persist the user in the database
			
			userManager.registerUser(usrn, pwd, email, isEmployee);
		} catch (Exception e) {
			
			// a error occurred, probably the user have chosen a password or an username already chosen by another one
			
			e.printStackTrace();
			printPage("User with same username or password already exsisting", response.getWriter());
			
			return;
		}	
		
		// notify the user that he registered successfully and redirect him to the login page again
		
		printPage("Registration succesfully completed", response.getWriter());
		
	}
	
	private void printPage(String errorMessage, @NotNull PrintWriter out) {
		
		//response.sendRedirect(getServletContext().getContextPath() + "/LoginPage.html");
		
		new HTMLPrinter(out, "LandingPage").printLoginPage("", errorMessage);
	}
	

}
