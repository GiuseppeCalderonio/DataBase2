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
			usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
			pwd = StringEscapeUtils.escapeJava(request.getParameter("password"));
			email = StringEscapeUtils.escapeJava(request.getParameter("email"));
			String temp = (request.getParameter("isEmployee"));
			isEmployee = temp != null;
			System.out.println("ATTENZIONE "+ isEmployee);
			
			
			if (usrn == null || pwd == null || email == null ||
					usrn.isEmpty() || pwd.isEmpty() || !email.contains("@")){
				throw new Exception("Missing or empty credential value");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			printPage(e.getMessage(), response.getWriter());
			return;
		}
		try {
			userManager.registerUser(usrn, pwd, email, isEmployee);
		} catch (Exception e) {
			e.printStackTrace();
			printPage("User with same username or password already exsisting", response.getWriter());
			
			return;
		}	
		
		printPage("Registration succesfully completed", response.getWriter());
		
	}
	
	private void printPage(String errorMessage, @NotNull PrintWriter out) {
		
		//response.sendRedirect(getServletContext().getContextPath() + "/LoginPage.html");
		
		new HTMLPrinter(out, "LandingPage").printLoginPage("", errorMessage);
	}
	

}
