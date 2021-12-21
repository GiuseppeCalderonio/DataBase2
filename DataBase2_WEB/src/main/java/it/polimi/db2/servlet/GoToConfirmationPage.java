package it.polimi.db2.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
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
import it.polimi.db2.entities.Order;
import it.polimi.db2.entities.Package;
import it.polimi.db2.entities.User;
import it.polimi.db2.exceptions.OrderException;
import it.polimi.db2.exceptions.PackagesNotFoundException;
import it.polimi.db2.jee.stateless.OrderManager;
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
	
	@EJB(name = "it.polimi.db2.jee.stateless/OrderManager")
	private OrderManager orderManager;
       
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
		
		System.out.println("GOTOCONFIRMANTIONPAGE GET");
		
		// ["userId", "confirmationFlag", "packageid", "validityPeriod", "startDate", "optionalProductsChosen"]
		
		Package packageChosen = null;
		int validityPeriod = 0;
		List<OptionalProduct> optionalProductsChosen = null;
		Date startDate = null;
		
		String resumedOrder = null;
		
		try {
				
			// when the user has already done the buy service process
		
			request.getSession().setAttribute("confirmationFlag", true);
			
			// get the package chosen by the user (we assume that if the user has arrived here it is because he have already selected the package)
			
			packageChosen = packageManager.getPackage( (int) request.getSession().getAttribute("packageid"));
			
			// get the validity period chosen by the user
			
			validityPeriod = (int)request.getSession().getAttribute("validityPeriod");
			
			// get the startDate
			
			startDate = (Date) request.getSession().getAttribute("startDate");
			
			// get the optional Products chosen
			
			List<String> optionalPorductsNames = (List<String>) request.getSession().getAttribute("optionalProductsChosen");
						
			optionalProductsChosen = optionalPorductsNames.stream().map(opName -> packageManager.getOptionalProduct(opName)).toList();
			
			// verify if the user has logged in
			
			boolean isLogged;
			
			try {
				isLogged = userManager.findById((int)request.getSession().getAttribute("userId")) != null;
				
				// verify if the user is employee
				
				if(userManager.findById((int)request.getSession().getAttribute("userId")).isEmployee()) {
					
					// come back to login page
					
					String path = getServletContext().getContextPath() + "/LoginServlet";
					response.sendRedirect(path);
					return;
				}
				
			}catch (NullPointerException e) {
				isLogged = false;
			}
			
			
			// print the page with the info relative to the order to create
			
			printPage("", response.getWriter(), packageChosen, validityPeriod, optionalProductsChosen, startDate, isLogged);
			
		} catch(NullPointerException e) { // user didn't the process right [birbantello]
			
			// for debugging purposes
			e.printStackTrace();
			
			response.sendRedirect(getServletContext().getContextPath() + "/GoToHomePage");
			
		}
		
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ideally creates the order
		
		// to verify if the payment is positive or negative use request.getParameter("payment") that can be the string "true" or "false"
		
		String errorMessage = "";
		
		// ASSUMPTION: buy button won't never show if the user is not logged in, so this is always different from zero, and a user can't delete his account
		
		User user = userManager.findById((int)request.getSession().getAttribute("userId"));
		
		try {
			
			if(request.getSession().getAttribute("orderId") != null){
				
				int orderId = (int)request.getSession().getAttribute("orderId");
				String validity = request.getParameter("payment");
				boolean isValid = validity.equals("true");
				
				orderManager.updateOrder(orderId, isValid);
				
				deleteAttributes(request);
				
				String path = getServletContext().getContextPath() + "/GoToHomePage";
				response.sendRedirect(path);
				
				return;
				
			}
			
			
			
			
			// get the package chosen by the user (we assume that if the user has arrived here it is because he have already selected the package)
			
			Package pack = packageManager.getPackage( (int) request.getSession().getAttribute("packageid"));
			
			// get the optional Products chosen
			
			List<String> optionalPorductsNames = (List<String>) request.getSession().getAttribute("optionalProductsChosen");
			List<OptionalProduct> optionalProducts = optionalPorductsNames.stream().map(opName -> packageManager.getOptionalProduct(opName)).toList();
			
			// get the startDate
			
			Date startDate = (Date) request.getSession().getAttribute("startDate");
			
			// get the validity of the payment [positive or negative]
			
			String validity = request.getParameter("payment");
			
			// set the default to true if the user forgets about it
			
			boolean isValid = true;
			try {
				isValid = validity.equals("true");
			} catch(NullPointerException e) {
				isValid = true;
			}
			
			
			// get the validity period chosen by the user
			
			int validityPeriod = (int)request.getSession().getAttribute("validityPeriod");
			
			orderManager.createOrder(user, pack, optionalProducts, startDate, isValid, validityPeriod);
			
			deleteAttributes(request);
			
		} catch (ParseException | OrderException | NullPointerException e) {
			
			errorMessage = "errore nel caricamento della pagina";
			
			try {
				
				int userId = (int) request.getSession().getAttribute("userId");
				
				List<String> failedOrders = new ArrayList<>();
					
				//get the list of orders associated with the user of this session IF exists
					
				failedOrders = orderManager.getInsolventOrdersOf(userId).stream().map(order -> String.valueOf(order) ).toList();
				
				
				new HTMLPrinter(response.getWriter(), "HomePage").printHomePage(errorMessage, packageManager.getPackages(), user.getUsername(), failedOrders);
				
			} catch (IOException | PackagesNotFoundException e1) {
				
				// problem
				
				e1.printStackTrace();
				
			}
		}
		
		String path = getServletContext().getContextPath() + "/GoToHomePage";
		response.sendRedirect(path);
	}
	
	
	private void printPage(String errorMessage, @NotNull PrintWriter out, Package packageChosen, int validityPeriod,
			List<OptionalProduct> optionalProductsChosen, Date startDate, boolean isLogged) {
		
		new HTMLPrinter(out, "ConfirmationPage").printConfirmationPage(errorMessage, packageChosen, validityPeriod, optionalProductsChosen , startDate, isLogged);
		
	}
	
	private void deleteAttributes(HttpServletRequest request) {
		
		// ["userId", "confirmationFlag", "packageid", "validityPeriod", "startDate", "optionalProductsChosen"]
		
		request.getSession().removeAttribute("packageid");
		request.getSession().removeAttribute("validityPeriod");
		request.getSession().removeAttribute("startDate");
		request.getSession().removeAttribute("optionalProductsChosen");
		request.getSession().removeAttribute("orderId");
		
		// ["userId", "confirmationFlag"]
	}

}
