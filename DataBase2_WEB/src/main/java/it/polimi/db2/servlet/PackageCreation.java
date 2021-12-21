package it.polimi.db2.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.ejb.EJB;
import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.db2.entities.FixedPhone;
import it.polimi.db2.entities.MobileInternet;
import it.polimi.db2.Creation;
import it.polimi.db2.HTMLhelper.HTMLPrinter;
import it.polimi.db2.entities.FixedInternet;
import it.polimi.db2.entities.MobilePhone;
import it.polimi.db2.entities.OptionalProduct;
import it.polimi.db2.exceptions.OptinalProductsNotFoundException;
import it.polimi.db2.jee.stateless.EmployeeManager;
import it.polimi.db2.jee.stateless.UserManager;

/**
 * Servlet implementation class PackageCreation
 */
@WebServlet("/PackageCreation")
public class PackageCreation extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name = "it.polimi.db2.jee.stateless/UserManager")
	private UserManager userManager;
	
	@EJB(name = "it.polimi.db2.jee.stateless/EmployeeManager")
	private EmployeeManager employeeManager;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PackageCreation() {
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
		int fee12;
		int fee24;
		int fee36;
		
		try { // to get the parameters
			
			// get the name of the package
			
			name = request.getParameter("name");
			
			// get the monthly fee for 12 months
			
			fee12 = Integer.parseInt(request.getParameter("fee12"));
			
			// get the monthly fee for 24 months
			
			fee24 = Integer.parseInt(request.getParameter("fee24"));
			
			// get the monthly fee for 36 months
			
			fee36 = Integer.parseInt(request.getParameter("fee36"));
			
			// get the optionalProducts selected
			
			List<String> namesSelected;
			
			List<OptionalProduct> AllOptionalProducts = employeeManager.getOptionalProducts();
			
			namesSelected = AllOptionalProducts.stream().
					map(op -> op.getName()).
					filter(n -> request.getParameter(n) != null).
					toList();
			
			List<OptionalProduct> optionalProducts = AllOptionalProducts.stream().
					filter(op -> namesSelected.contains(op.getName())).
					toList();
			
			// get the mobile phone service IF exists
			
			MobilePhone mobilePhone = null;
			String mobilePhoneId = request.getParameter("mobilePhoneService");
			if(mobilePhoneId != null) {
				mobilePhone = employeeManager.getMobilePhone(Integer.parseInt(mobilePhoneId));
			}
			
			// get the mobile Internet service IF exists
			
			MobileInternet mobileInternet = null;
			String mobileInternetId = request.getParameter("MobileInternetService");
			if(mobileInternetId != null) {
				mobileInternet = employeeManager.getMobileInternet(Integer.parseInt(mobileInternetId));
			}
			
			// get the fixed Internet service IF exists
			
			FixedInternet fixedInternet = null;
			String fixedInternetId = request.getParameter("FixedInternetService");
			if(fixedInternetId != null) {
				fixedInternet = employeeManager.getFixedInternet(Integer.parseInt(fixedInternetId));
			}
			
			// get the fixed phone IF exists
			
			String temp = request.getParameter("fixedPhoneService");
			FixedPhone fixedPhone = null;
			
			if(temp != null) {
				fixedPhone = new FixedPhone();
				fixedPhone.setServiceid(1);
			}	
			
			// create the package
			
			employeeManager.addPackage(name,
					optionalProducts,
					fixedInternet,
					mobileInternet,
					fixedPhone,
					mobilePhone,
					fee12,
					fee24,
					fee36);
			
			// print the page
			
			printPage(response.getWriter(), "Service correctly created", username);
			
			
		}catch (Exception e) {
			// somethong went wrong
			
			printPage(response.getWriter(), "Something went wrong while creatig the package", username);
		}
	}
	
	
	private void printPage(PrintWriter out, String errorMessage, String username) {
		new HTMLPrinter(out, "EmployeeHomePage").
		printEmployeeHomePage(errorMessage,
				username,
				Creation.PACKAGE,
				null, null, null, null, null);
	}
	

}
