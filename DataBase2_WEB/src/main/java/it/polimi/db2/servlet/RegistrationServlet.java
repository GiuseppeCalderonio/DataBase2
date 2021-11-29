package it.polimi.db2.servlet;

import java.io.IOException;

import javax.ejb.EJB;
import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;

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
		
		String path = getServletContext().getContextPath() + "/RegistrationPage.html";
		response.sendRedirect(path);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String usrn = null;
		String pwd = null;
		String email = null;
		try {
			usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
			pwd = StringEscapeUtils.escapeJava(request.getParameter("password"));
			email = StringEscapeUtils.escapeJava(request.getParameter("email"));
			
			if (usrn == null || pwd == null || email == null ||
					usrn.isEmpty() || pwd.isEmpty() || !email.contains("@")){
				throw new Exception("Missing or empty credential value");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			doGet(request, response);
			return;
		}
		try {
			userManager.registerUser(usrn, pwd, email);
		} catch (Exception e) {
			e.printStackTrace();
			doGet(request, response);
			
			return;
		}	
		
		String path = getServletContext().getContextPath() + "/LoginPage.html";
		response.sendRedirect(path);
		
	}

}
