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
 * Servlet implementation class OptionalProductCreation
 */
@WebServlet("/OptionalProductCreation")
public class OptionalProductCreation extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name = "it.polimi.db2.jee.stateless/UserManager")
	private UserManager userManager;
	
	@EJB(name = "it.polimi.db2.jee.stateless/EmployeeManager")
	private EmployeeManager employeeManager;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OptionalProductCreation() {
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
		
		String name;
		int fee;
		
		try { // to get the parameters
			
			// get the name
			
			name = request.getParameter("name");
			
			// get the fee
			
			fee = Integer.parseInt(request.getParameter("fee"));
			
			// if the name is empty do not allow the creation
			
			if(name.equals(""))
				throw new NullPointerException("cannot use the empty string");
			
			// create the optional product
			
			employeeManager.addOptionalProduct(name, fee);
			
			// print the page
			
			printPage(response.getWriter(), "Optional product correctly created", username);
			
			
		}catch (NumberFormatException | NullPointerException | PersistenceException   e) {
			printPage(response.getWriter(), e.getMessage(), username);
		}
		
	}
	
	private void printPage(PrintWriter out, String errorMessage, String username) {
		new HTMLPrinter(out, "EmployeeHomePage").
		printEmployeeHomePage(errorMessage,
				username,
				Creation.OPTIONALPRODUCT,
				null, null, null, null);
	}

}
