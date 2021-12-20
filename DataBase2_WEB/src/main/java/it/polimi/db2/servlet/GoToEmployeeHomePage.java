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

import it.polimi.db2.Creation;
import it.polimi.db2.Service;
import it.polimi.db2.HTMLhelper.HTMLPrinter;
import it.polimi.db2.entities.FixedPhone;
import it.polimi.db2.entities.MobileInternet;
import it.polimi.db2.entities.FixedInternet;
import it.polimi.db2.entities.MobilePhone;
import it.polimi.db2.entities.OptionalProduct;
import it.polimi.db2.entities.Package;
import it.polimi.db2.exceptions.OptinalProductsNotFoundException;
import it.polimi.db2.exceptions.ServiceNotFoundException;
import it.polimi.db2.jee.stateless.EmployeeManager;
import it.polimi.db2.jee.stateless.UserManager;


/**
 * Servlet implementation class GoToEmployeeHomePage
 */
@WebServlet("/GoToEmployeeHomePage")
public class GoToEmployeeHomePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name = "it.polimi.db2.jee.stateless/UserManager")
	private UserManager userManager;
	
	@EJB(name = "it.polimi.db2.jee.stateless/EmployeeManager")
	private EmployeeManager employeeManager;
	
       
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
		List<OptionalProduct> optionalProducts = new ArrayList<>();
		List<FixedInternet> fixedInternetServices = new ArrayList<>();
		List<MobileInternet> mobileInternetServices = new ArrayList<>();
		List<FixedPhone> fixedPhoneServices = new ArrayList<>();
		List<MobilePhone> mobilePhoneServices = new ArrayList<>();
		
		try {
			
			// get the user id associated with the user of this session IF exists
			
			userId = (int) request.getSession().getAttribute("userId");
			
			// get the username associated with the user of this session IF exists
			
			username = userManager.findById(userId).getUsername();
			
			// verify if the user is employee
			
			if(!userManager.findById(userId).isEmployee()) throw new NullPointerException();
			
			}catch(NullPointerException e) {
			
			// the user didn't login
			
			username = null;
			
			// redirect the user to the login page (if not logged, he cannot access this page)
			// and finish the execution of the get
			
			String path = getServletContext().getContextPath() + "/LoginServlet";
			response.sendRedirect(path);
			return;
		}
		
		Creation creation = getCreation(request);
		
		String errorMessage = "";
		
		fillFields(creation,
				optionalProducts,
				fixedInternetServices,
				mobileInternetServices,
				fixedPhoneServices,
				mobilePhoneServices,
				errorMessage);
		
		printPage(errorMessage,
				response.getWriter(),
				username,
				creation,
				optionalProducts,
				fixedInternetServices,
				mobileInternetServices,
				fixedPhoneServices,
				mobilePhoneServices);
		
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private void printPage(String errorMessage,
			@NotNull PrintWriter out,
			String username,
			Creation toCreate,
			List<OptionalProduct> optionalProducts,
			List<FixedInternet> fixedInternetServices,
			List<MobileInternet> mobileInternetServices,
			List<FixedPhone> fixedPhoneServices,
			List<MobilePhone> mobilePhoneServices) {
		
		new HTMLPrinter(out, "EmployeeHomePage").printEmployeeHomePage(errorMessage,
				username,
				toCreate,
				optionalProducts,
				fixedInternetServices,
				mobileInternetServices,
				fixedPhoneServices,
				mobilePhoneServices);
	}
	
	private Creation getCreation(HttpServletRequest request) {
		
		try {
			
			return Creation.valueOf(Creation.class, request.getParameter("toCreate"));
			
		}catch(NullPointerException e ) {
			
			return null;
			
		}
	}
	
	private void fillFields(Creation creation, 
			List<OptionalProduct> optionalProducts,
			List<FixedInternet> fixedInternetServices,
			List<MobileInternet> mobileInternetServices,
			List<FixedPhone> fixedPhoneServices,
			List<MobilePhone> mobilePhoneServices,
			String errorMessage) {
		
		if (creation == null) return;
		if (creation.equals(Creation.PACKAGE)) {
			
			// to get the optional products and services available
			
			try { 
				
				optionalProducts.addAll(employeeManager.getOptionalProducts());
				fixedInternetServices.addAll(employeeManager.getFixedInternetServices());
				mobileInternetServices.addAll(employeeManager.getMobileInternetServices());
				fixedPhoneServices.addAll(employeeManager.getFixedPhoneServices());
				mobilePhoneServices.addAll(employeeManager.getMobilePhoneServices());
				
			} catch (OptinalProductsNotFoundException | ServiceNotFoundException | NullPointerException e) {
				// problems
				e.printStackTrace();
				errorMessage = e.getMessage();
			}
		}
	}

}
