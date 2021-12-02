package it.polimi.db2.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
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
import it.polimi.db2.entities.OptionalProduct;
import it.polimi.db2.entities.Package;
import it.polimi.db2.jee.stateless.PackageManager;
import it.polimi.db2.jee.stateless.UserManager;

/**
 * Servlet implementation class GoToConfirmationPage
 */
@WebServlet("/GoToConfirmationPage")
public class GoToConfirmationPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name = "it.polimi.db2.jee.stateless/PackageManager")
	private PackageManager packageManager;
	
	@EJB(name = "it.polimi.db2.jee.stateless/UserManager")
	private UserManager userManager;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToConfirmationPage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// ["userId", "confirmationFlag", "packageid", "validityPeriod", "startDate", "optionalProductsChosen"]
		
		Package packageChosen = null;
		int validityPeriod = 0;
		List<OptionalProduct> optionalProductsChosen = null;
		Date startDate = null;
		
		// when the user has already done the buy service process
		
		try {
			request.getSession().setAttribute("confirmationFlag", true);
			
			// get the package chosen by the user (we assume that if the user has arrived here it is because he have already selected the package)
			
			packageChosen = packageManager.getPackage( (int) request.getSession().getAttribute("packageid"));
			
			// get the fee chosen by the user
			
			validityPeriod = (int)request.getSession().getAttribute("validityPeriod");
			
			// get the startDate
			
			startDate = (Date) request.getSession().getAttribute("startDate");
			
			// get the optional Product chosen
			
			List<String> optionalPorductsNames = (List<String>) request.getSession().getAttribute("optionalProductsChosen");
			optionalProductsChosen = optionalPorductsNames.stream().map(opName -> packageManager.getOptionalProduct(opName)).toList();
			
			// verify if the user has logged in
			
			boolean isLogged;
			
			try {
				isLogged = userManager.findById((int)request.getSession().getAttribute("userId")) != null;
			}catch (NullPointerException e) {
				isLogged = false;
			}
			
			
			// print the page with the info relative to the order to create
			
			printPage("", response.getWriter(), packageChosen, validityPeriod, optionalProductsChosen, startDate, isLogged);
			
		} catch(NullPointerException e) { // user didn't the process right [birbantello]
			
			response.sendRedirect(getServletContext().getContextPath() + "/GoToHomePage");
			
		}
		
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ideally creates the order
		
		// to verify if the payment is positive or negative use request.getParameter("payment") that can be the string "true" or "false"
		
		doGet(request, response);
	}
	
	
	private void printPage(String errorMessage, @NotNull PrintWriter out, Package packageChosen, int validityPeriod,
			List<OptionalProduct> optionalProductsChosen, Date startDate, boolean isLogged) {
		
		new HTMLPrinter(out, "ConfirmationPage").printConfirmationPage(errorMessage, packageChosen, validityPeriod, optionalProductsChosen , startDate, isLogged);
		
	}

}
