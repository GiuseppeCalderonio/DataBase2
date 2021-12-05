package it.polimi.db2.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import it.polimi.db2.Creation;
import it.polimi.db2.HTMLhelper.HTMLPrinter;
import it.polimi.db2.entities.Package;
import it.polimi.db2.jee.stateless.UserManager;

/**
 * Servlet implementation class GoToEmployeeHomePage
 */
@WebServlet("/GoToEmployeeHomePage")
public class GoToEmployeeHomePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name = "it.polimi.db2.jee.stateless/UserManager")
	private UserManager userManager;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToEmployeeHomePage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
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
			
			String path = getServletContext().getContextPath() + "/LoginServlet";
			response.sendRedirect(path);
		}
		
		Creation toCreate = null;
		
		try {
			/*if  (request.getParameter("toCreate").equals("PACKAGE")) {
				toCreate = Creation.PACKAGE;
				System.out.println(toCreate);
			}
			else  {
				if (request.getParameter("toCreate").equals("OPTIONALPRODUCT")) 
					toCreate = Creation.OPTIONALPRODUCT;
				}*/
			
			 switch (request.getParameter("toCreate")) {
				case "PACKAGE":
					toCreate = Creation.PACKAGE;
					break;
					
				case "OPTIONALPRODUCT":
					toCreate = Creation.OPTIONALPRODUCT;
					break;
					
				case "MOBILEPHONE":
					toCreate = Creation.MOBILEPHONE;
					break;
					
				case "FIXEDINTERNET":
					toCreate = Creation.FIXEDINTERNET;
					break;
					
				case "MOBILEINTERNET":
					toCreate = Creation.MOBILEINTERNET;
					break;
					
				default:
					System.out.println("Somenthing wrong happened");
					break;
			}
			
		} catch (NullPointerException e) {
			
		}
		
		printPage("", response.getWriter(), username, toCreate);
		
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private void printPage(String errorMessage, @NotNull PrintWriter out, String username, Creation toCreate) {
		
		new HTMLPrinter(out, "EmployeeHomePage").printEmployeeHomePage(errorMessage, username, toCreate);
	}

}
