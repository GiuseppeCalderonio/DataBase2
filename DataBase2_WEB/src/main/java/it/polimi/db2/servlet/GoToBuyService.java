package it.polimi.db2.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringEscapeUtils;

import it.polimi.db2.HTMLhelper.HTMLPrinter;
import it.polimi.db2.entities.OptionalProduct;
import it.polimi.db2.entities.Order;
import it.polimi.db2.entities.Package;
import it.polimi.db2.exceptions.PackagesNotFoundException;
import it.polimi.db2.jee.stateless.PackageManager;

/**
 * Servlet implementation class GoToBuyService
 */
@WebServlet("/GoToBuyService")
public class GoToBuyService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	
	@EJB(name = "it.polimi.db2.jee.stateless/PackageManager")
	private PackageManager packageManager;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToBuyService() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("GET");
		
		List <Package> packages = new ArrayList<>();
		
		try {
			
			// get all the available packages from the database
			
			 packages = packageManager.getPackages();
			 
		} catch (PackagesNotFoundException e) {
			
			// problem
			e.printStackTrace();
		}
		
		Package packageChosen;
		
		try {
			
			// get the package previously chosen by the user in the session IF exists
			
			packageChosen = packageManager.getPackage(Integer.parseInt(request.getParameter("packageid")));
			
			// set the attribute packageid of the session to store the info about the package
			
			request.getSession().setAttribute("packageid", packageChosen.getPackageid()); 
			
		}catch(NumberFormatException e) { // if the user didn't select any package previously
			//e.printStackTrace();
			
			packageChosen = null;
		}
		
		// print the page
		
		printPage("", response.getWriter(), packages, packageChosen);
		
		// here we have two possibilities on the content of the attributes of the session:
		// 1) WITH LOGIN ["userId", "confirmationFlag", "packageid"]
		// 2) WITHOUT LOGIN ["confirmationFlag", "packageid"]
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// retrieve the package chosen to see the available optional products names (keys)
		
		Package packageChosen = packageManager.getPackage((int)request.getSession().getAttribute("packageid"));
		
		// store the start date chosen
		
		try {
			
			request.getSession().setAttribute("startDate", translateInDate(request.getParameter("startDate")));
			
		} catch(Exception e) {

			System.out.println(e.getMessage());
			
			try {
				
				printPage(e.getMessage(), response.getWriter(), packageManager.getPackages(), packageChosen);
				return;
				
			} catch (IOException | PackagesNotFoundException e1) {
				// problem
				e1.printStackTrace();
				
				doGet(request, response);
			}
		}
		
		// retrieve the validityPeriod
		
		String validityPeriod = StringEscapeUtils.escapeJava(request.getParameter("validityPeriod"));
		
		// store the validity period chosen
		
		request.getSession().setAttribute("validityPeriod", Integer.parseInt(validityPeriod));
		
		// get in a list the optional products of a package
		
		List<String> optionalProducts = packageChosen.getOptionalProducts()
				.stream().
				map(op -> op.getName()).
				toList();
		
		// filter only the optional products of the package chosen that were selected by the user
		
		List<String> optionalProductsChosen = new ArrayList<>();
		
		for(String op : optionalProducts) {
			if(request.getParameter(op) != null) {
				optionalProductsChosen.add(op);
			}
		}
		
		//optionalProductsChosen.stream().
		//filter(op -> request.getParameter(op) != null /*&& request.getParameter(op).equals("on")*/).
		//toList();
		
		// store the optional products chosen
		
		request.getSession().setAttribute("optionalProductsChosen", optionalProductsChosen);
		
		// switch to the confirmation page
		
		String path = getServletContext().getContextPath() + "/GoToConfirmationPage";
		response.sendRedirect(path);
		
		// here we have two possibilities on the content of the attributes of the session:
		// 1) WITH LOGIN ["userId", "confirmationFlag", "packageid", "validityPeriod", "startDate", "optionalProductsChosen"]
		// 2) WITHOUT LOGIN [ "confirmationFlag", "packageid", "validityPeriod", "startDate", "optionalProductsChosen"]
	}

	
	private void printPage(String errorMessage, @NotNull PrintWriter out, List<Package> packages, Package packageChosen) {
		
		new HTMLPrinter(out, "BuyServicePage").printBuyServicePage(errorMessage, packages, packageChosen);
	}
	
	private Date translateInDate(String date) throws Exception {
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		Date startDate;
		
		try {
			startDate = new java.sql.Date(df.parse(date).getTime());
		}catch(ParseException e) {
			throw new Exception("Data format wrong, be sure to insert a date like 2000-01-01"); 
		}
		
		
		if(startDate.before(new java.util.Date()))
				throw new Exception("You cannot insert a date before that one of today");
		
		return startDate;
	}
	
}
