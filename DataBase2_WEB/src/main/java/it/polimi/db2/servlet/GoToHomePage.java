package it.polimi.db2.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import it.polimi.db2.HTMLhelper.HTMLPrinter;
import it.polimi.db2.exceptions.PackagesNotFoundException;
import it.polimi.db2.jee.stateless.PackageManager;
import it.polimi.db2.jee.stateless.UserManager;
import it.polimi.db2.entities.*;
import it.polimi.db2.entities.Package;

/**
 * Servlet implementation class GoToHomePage
 */
@WebServlet("/GoToHomePage")
public class GoToHomePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	@EJB(name = "it.polimi.db2.jee.stateless/PackageManager")
	private PackageManager packageManager;
	
	@EJB(name = "it.polimi.db2.jee.stateless/UserManager")
	private UserManager userManager;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToHomePage() {
        super();
        
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List <Package> packages = new ArrayList<>();
		
		
		try {
			
			// get all the packages available
			
			 packages = packageManager.getPackages();
		} catch (PackagesNotFoundException e) {
			
			// something bad happened
			
			e.printStackTrace();
			
			printPage("Something bad happened: " + e.getMessage(), response.getWriter(), packages, null);
			
		}
		
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
		}

		// print the home page, showing the packages and eventually the username
		
		printPage("", response.getWriter(), packages, username);
		
		// set the confirmation flag to false, it means that if the user goes to the login page, this page here will be the next one
		
		request.setAttribute("confirmationFlag", false);
		
		// from now there are two possible lists of session attributes
		// 1) ["userId", "confirmationFlag"]
		// 2) ["confirmationFlag"]
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// not used
		
		doGet(request, response);
	}
	
	
	private void printPage(String errorMessage, @NotNull PrintWriter out, List<Package> packages, String username) {
	
		new HTMLPrinter(out, "HomePage").printHomePage(errorMessage, packages, username);
	}
	

}
