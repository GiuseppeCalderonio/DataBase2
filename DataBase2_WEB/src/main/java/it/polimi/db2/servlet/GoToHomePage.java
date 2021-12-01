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
			 packages = packageManager.getPackages();
		} catch (PackagesNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int userId;
		String username;
		
		try {
			userId = (int) request.getSession().getAttribute("userId");
			username = userManager.findById(userId).getUsername();
			
		}catch(NullPointerException e) {
			username = null;
		}

		printPage("", response.getWriter(), packages, username);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
	private void printPage(String errorMessage, @NotNull PrintWriter out, List<Package> packages, String username) {
	
		new HTMLPrinter(out, "HomePage").printHomePage(errorMessage, packages, username);
	}
	

}
