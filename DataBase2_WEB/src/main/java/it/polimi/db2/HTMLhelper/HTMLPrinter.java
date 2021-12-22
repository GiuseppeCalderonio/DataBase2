package it.polimi.db2.HTMLhelper;

import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import it.polimi.db2.entities.*;
import it.polimi.db2.entities.materializedViews.*;
import it.polimi.db2.entities.Package;
import it.polimi.db2.Creation;
import it.polimi.db2.Service;

public class HTMLPrinter {
	
	private PrintWriter out;
	private String title;
	
	public HTMLPrinter(PrintWriter out, String title) {
		this.out = out;
		this.title = title;
	}
	
	public void printLoginPage(String loginError, String registrationError) {
		
		// generate the form relative to the login
		
		Form loginForm = generateLoginForm(loginError);
		
		// generate the form for the registration
		
		Form registrationForm = generateRegistrationForm(registrationError);
		
		// generate the form for the home page redirect
		
		Form goToHomePage = generateHomePageForm();
		
		
		printHeader();
		out.println("<body>");
		
		out.println("<h1>Welcome to the Service Telco Application</h1>");
		out.println(loginForm.toString());
		out.println("<h1> Registration Form</h1>");
		out.println(registrationForm.toString());
		out.println(goToHomePage.toString());
		
		out.println("</body>");
		out.println("</html>");
		
	}
	
	private Form generateLoginForm(String error) {
		
		List<FormInstance> loginInstances = new ArrayList<>();
		
		loginInstances.add(new FormInstance("Username", "text", "username"));
		loginInstances.add(new FormInstance("Password", "password", "pwd"));
		
		return new Form("LoginServlet", "POST", error, loginInstances, "login");
	}
	
	private Form generateRegistrationForm(String error) {
		
		List<FormInstance> formInstances = new ArrayList<>();
		
		formInstances.add(new FormInstance("Username","text","username", true));
		formInstances.add(new FormInstance("Email", "email", "email"));
		formInstances.add(new FormInstance("Password", "password", "password"));
		formInstances.add(new FormInstance("Register as Employee","checkbox","isEmployee", false));
		
		return new Form("RegistrationServlet", "POST", error, formInstances, "registration");
		
	}
	
	private Form generateHomePageForm() {
		
		return new Form("GoToHomePage", "GET", "", new ArrayList<FormInstance>(), "Home Page");
	}
	
	public void printHomePage(String homePageError, List<Package> packages, String username, List<String> failedOrders) {
		printHeader();
		out.println("<body>");
		out.println("<h1>here they are our packages, if you want to pursue an order press the button Buy Service </h1>");
		
		
		
		String toShow = "Login";
		
		if(username != null) { // user logged in
			out.println("Hi " + username + "<br>");
			toShow = "LogOut";
		}
			
		// Login and Logout basically do the same thing, they just display a different user
		// interface button to do it
		
		Form goToLogin = new Form("LoginServlet", "GET", "", null, toShow);
		out.println(goToLogin.toString());
		
		// print a form to buy a service package
		
		Form buyServicePackage = new Form("GoToBuyService", "GET", "", null, "Buy a Service");
		out.println(buyServicePackage.toString());
		
		// print the list of packages
		
		out.println("<ul>");
		
		// print all the packages
		
		for(Package p : packages) {
			out.print("<li>");
			out.println(p.toString());
			out.print("</li>");
		}
		
		out.println("</ul>");
		
		if (!failedOrders.isEmpty()) {
			out.println("<b><br> Failed Orders </br> </b>");
			
			List<FormInstance> failedOrdersInstances = new ArrayList<>();
			
			for(String s : failedOrders) {
				failedOrdersInstances.add(new FormInstance("Orderid:" + s.toString() ,"radio", "FailedOrders", s.toString() + ""));
			}
			
			out.println(new Form("GoToHomePage", "POST", " ", failedOrdersInstances, "choose"));
			
		}
		
		
		
		out.println("</body>");
		out.println("</html>");
	}
	
	public void printBuyServicePage(String errorMessage, List<Package> packages, Package packageChosen) {
		printHeader();
		out.println("<body>");
		out.println("<h1>Choose only one service from those available to buy it and fill the info </h1>");
		
		out.println("<form action = \"GoToBuyService\" method = \"GET\">");
		out.println("<label for=\"Package\">Choose a Package:</label>");
		out.println("<select name = \"packageid\">");
		
		for(Package p : packages) {
			out.println("<option value = \"" + p.getPackageid() + "\">" + p.getName() + "</option>");
		}
		
		out.println("<select name = \"id\">");
		out.println("<input type=\"submit\" value=\"Submit\">");
		out.println("</form>");
		
		if(packageChosen != null) {
			
			out.println("<br>Package info: " + packageChosen.toString());
			
			List<FormInstance> formInstances = new ArrayList<>();
			
			String validityPeriodOption = "<label for=\"validityPeriod\">Choose a validity period:</label>\r\n"
					+ "		  <select name=\"validityPeriod\">\r\n"
					+ "		    <option value=\"12\">12 Months</option>\r\n"
					+ "		    <option value=\"24\">24 Months</option>\r\n"
					+ "		    <option value=\"36\"> 36 Months</option>\r\n"
					+ "		  </select>";
			
			formInstances.add(new FormInstance("Start date(yyyy-mm-dd) ","date","startDate"));
			
			for(OptionalProduct op : packageChosen.getOptionalProducts()) {
				formInstances.add(new FormInstance(op.toString() ,"checkbox",op.getName(), false));
				
			}
			
			Form buyPackage = new Form("GoToBuyService", "POST", "", formInstances, "Confirm Payment", validityPeriodOption);
			out.print(buyPackage.toString());
		}
		
		out.println("<br>" + errorMessage);
		out.println("</body>");
		out.println("</html>");
	}
	
	public void printConfirmationPage(String errorMessage, Package packageChosen, int validityPeriod, List<OptionalProduct> optionalProductsChosen, Date startDate, boolean isLogged) {
		printHeader();
		out.println("<body>");
		out.println("<h1> That's a recap of what you chose, check if all the data are correct </h1>");
		
		// print the package chosen
		
		out.println("<br>Package chose details: <br>" + packageChosen.toString());
		out.println("<br>");
		
		// print all the optional products chosen
		
		out.println("Optional products chosen among those available: <br>");
		out.println("<ul>");
		
		
		for(OptionalProduct op : optionalProductsChosen) {
			out.print("<li>");
			out.println(op.toString());
			out.print("</li>");
		}
		out.println("</ul>");
		
		// print the start date of the service
		
		out.println("<br>Service start date: " + startDate);
		
		// print the validityPeriod 
		
		out.println("<br>ValidityPeriod: " + validityPeriod);
		
		// print the total amount of money to be pre-paid
		
		out.println("<br>Amount to be pre-paid: " + getAmountToBePrePaid(packageChosen, validityPeriod, optionalProductsChosen));
		
		// print the forms to pay or the login page
		
		
		if(isLogged) {
			out.println(new Form("GoToConfirmationPage", "POST", "", 
					new ArrayList<FormInstance>(Arrays.asList(new FormInstance("positive", "radio", "payment", "true"), new FormInstance("negative", "radio", "payment", "false")))
					, "Payment"));
		}
		
		else {
			Form goToLogin = new Form("LoginServlet", "GET", "", null, "Login");
			out.println(goToLogin.toString());
		}
		
		out.println("</body>");
		out.println("</html>");
	}
	
	
	/**
	 * this method prints the home page for the employee in order to 
	 * support him in all the possible actions that he can do
	 * @param errorMessage this is the error message to eventually show
	 * @param username this is the username of the employee
	 * @param toCreate this is the form to create, and consecutively the action to do
	 * @param optionalProducts these are all the optional products of the db to eventually show when creating a package form
	 * @param fixedInternetServices these are all the fixed Internet services of the db to eventually show when creating a package form
	 * @param mobileInternetServices these are all the mobile Internet services of the db to eventually show when creating a package form
	 * @param fixedPhoneServices these are all the fixed phone services of the db to eventually show when creating a package form
	 * @param mobilePhoneServices these are all the mobile phone services of the db to eventually show when creating a package form
	 */
	public void printEmployeeHomePage(String errorMessage,
			String username,
			Creation toCreate,
			List<OptionalProduct> optionalProducts,
			List<FixedInternet> fixedInternetServices,
			List<MobileInternet> mobileInternetServices,
			List<FixedPhone> fixedPhoneServices,
			List<MobilePhone> mobilePhoneServices) {
		
		printHeader();
		out.println("<body>");
		out.println("<h1>In this page you can create a package with the associated optional products or an optional product</h1>");
		
		out.println("Hi " + username + "<br>");
		String toShow = "LogOut";
		
		Form goToLogin = new Form("LoginServlet", "GET", "", null, toShow);
		out.println(goToLogin.toString() + "<br>");
		
		out.println("<h3>" + errorMessage + "</h3>");
		
		out.println(new Form("SalesReportPage", "GET", "", null, "Sales Report Page"));
		
		////////////////////////////////////////////////////
		
		out.println("<form action = \"GoToEmployeeHomePage\" method = \"GET\">");
		String creationOptions = "<label for=\"toCreate\">Choose what you want to create:</label>\r\n"
				+ "		  <select name=\"toCreate\">\r\n"
				+ "		    <option value=\"PACKAGE\">Package</option>\r\n"
				+ "		    <option value=\"OPTIONALPRODUCT\">Optional Product</option>\r\n"
				+ "		    <option value=\"MOBILEPHONE\">Mobile phone service</option>\r\n"
				+ "		    <option value=\"FIXEDINTERNET\">Fixed internet service</option>\r\n"
				+ "		    <option value=\"MOBILEINTERNET\">Mobile internet service</option>\r\n"
				+ "		  </select>";
		out.println(creationOptions);
		out.println("<input type=\"submit\" value=\"Submit\">");
		out.println("</form>");
		
		///////////////////////////////////////////////////
		
		try {
		
			switch(toCreate) {
			
			case OPTIONALPRODUCT:
				optionalProductCase(errorMessage);
				//TODO check if fee is a number
				break;
			
			case PACKAGE:
				packageCase(optionalProducts,
						fixedInternetServices,
						mobileInternetServices,
						fixedPhoneServices ,
						mobilePhoneServices,
						errorMessage);
				break;
				
			case MOBILEPHONE:
				mobilePhoneCase(errorMessage);
				//TODO check are numbers
				break;
				
			case MOBILEINTERNET:
				
				mobileInternetCase(errorMessage);
				
				
				//TODO check are numbers
				break;
				
			case FIXEDINTERNET:
				
				fixedInternetCase(errorMessage);
				
				//TODO check are numbers
				break;
				
			default:
				break;
			}
		
		} catch (NullPointerException e) {
		}
		
		
		out.println("</body>");
		out.println("</html>");
	}
	
	private void printHeader() {
		out.println("<!DOCTYPE html>");
		out.println("<html xmlns:th=\"http://www.thymeleaf.org\">");
		out.println("<head>");
		out.println("<meta charset=\"ISO-8859-1\">");
		out.println("<title>" + title + "</title>");
		out.println("</head>");
	}
	
	private int getAmountToBePrePaid(Package packageChosen, int validityPeriod, List<OptionalProduct> optionalProducts) {
		int amount = 0;
		amount = amount + packageChosen.getFee(validityPeriod);
		for(OptionalProduct op : optionalProducts) {
			amount = amount + op.getFee();
		}
		
		return amount * validityPeriod;
		
	}
	
	private void optionalProductCase(String errorMessage) {
		out.println("<b> Optional product creation <br> </b>");
		
		List<FormInstance> optionalProductInstances = new ArrayList<>();
		
		optionalProductInstances.add(new FormInstance("Name", "text", "name", true));
		optionalProductInstances.add(new FormInstance("Fee", "int", "fee", true));
		
		Form optionalProductForm = new Form("OptionalProductCreation", "POST", errorMessage, optionalProductInstances, "create");
		out.println(optionalProductForm.toString());
	}
	
	private void mobilePhoneCase(String errorMessage) {
		
		out.println("<b> Mobile phone service creation <br> </b>");
		
		List<FormInstance> mobilePhoneInstances = new ArrayList<>();
		
		mobilePhoneInstances.add(new FormInstance("Minutes", "int", "minutes", true));
		mobilePhoneInstances.add(new FormInstance("SMS", "int", "sms", true));
		mobilePhoneInstances.add(new FormInstance("Fee extra minutes", "int", "minutesExtraFee", true));
		mobilePhoneInstances.add(new FormInstance("Fee extra SMS", "int", "smsExtraFee", true));
		
		Form mobilePhoneForm = new Form("MobilePhoneServiceCreation", "POST", errorMessage, mobilePhoneInstances, "create");
		out.println(mobilePhoneForm.toString());
		
	}
	
	private void mobileInternetCase(String errorMessage) {
		
		out.println("<b> Mobile internet service creation <br> </b>");
		
		List<FormInstance> mobileInternetInstances = new ArrayList<>();
		
		mobileInternetInstances.add(new FormInstance("GB", "int", "GIGA", true));
		mobileInternetInstances.add(new FormInstance("Fee extra GB", "int", "feeExtraGB", true));
		
		Form mobileInternetForm = new Form("MobileInternetServiceCreation", "POST", errorMessage, mobileInternetInstances, "create");
		out.println(mobileInternetForm.toString());
		
	}
	
	private void fixedInternetCase(String errorMessage) {
		
		out.println("<b> Fixed internet service creation <br> </b>");
		
		List<FormInstance> fixedInternetInstances = new ArrayList<>();
		
		fixedInternetInstances.add(new FormInstance("GB", "int", "GIGA", true));
		fixedInternetInstances.add(new FormInstance("Fee extra GB", "int", "feeExtraGB", true));
		
		Form fixedInternetForm = new Form("FixedInternetServiceCreation", "POST", errorMessage, fixedInternetInstances, "create");
		out.println(fixedInternetForm.toString());
		
	}
	
	private void packageCase( List<OptionalProduct> optionalProducts,
			List<FixedInternet> fixedInternetServices,
			List<MobileInternet> mobileInternetServices,
			List<FixedPhone> fixedPhoneServices,
			List<MobilePhone> mobilePhoneServices,
			String errorMessage) {
		
		out.println("<b> Package creation <br> </b>");
		
		List<FormInstance> packageInstances = new ArrayList<>();
		
		packageInstances.add(new FormInstance("Name", "text", "name"));
		packageInstances.add(new FormInstance("Monthly fee for 12 months", "int", "fee12"));
		packageInstances.add(new FormInstance("Monthly fee for 24 months", "int", "fee24"));
		packageInstances.add(new FormInstance("Monthly fee for 36 months", "int", "fee36"));
		
		for(OptionalProduct op : optionalProducts) {
			
			packageInstances.add(new FormInstance("Optional product :[" + op.toString() + " ]" ,"checkbox", op.getName(), false));
		}
		
		for(FixedInternet s : fixedInternetServices) {
			packageInstances.add(new FormInstance(s.toString() ,"radio", "FixedInternetService", s.getServiceid() + ""));
		}
		
		for(MobileInternet s : mobileInternetServices) {
			packageInstances.add(new FormInstance(s.toString() ,"radio", "MobileInternetService", s.getServiceid() + ""));
		}
		
		for(MobilePhone s : mobilePhoneServices) {
			packageInstances.add(new FormInstance(s.toString() ,"radio", "mobilePhoneService", s.getServiceid() + ""));
		}
		
		packageInstances.add(new FormInstance("Fixed Phone", "checkbox", "fixedPhoneService", false));
		
		out.println(new Form("PackageCreation", "POST", errorMessage, packageInstances, "create"));
		
	}
	
	
	public void printSalesReportPage(String errorMessage,
			List<MaterializedView1> mv1,
			List<MaterializedView2> mv2,
			List<MaterializedView3> mv3,
			List<MaterializedView4> mv4,
			MaterializedView6 bestSeller,
			List<MaterializedView5Insolvents> mv5Insolvents,
			List<MaterializedView5Suspended> mv5Suspended,
			List<AuditingTable> alerts) {
		printHeader();
		out.println("<body>");
		
		// print a general introduction of the page
		
		out.println("<h1>Welcome, this is the page in which you can see the sales reports</h1>");
		
		// print a form to redirect the employee to the previous page
		
		out.println(new Form("GoToEmployeeHomePage", "GET", errorMessage, null, "Go Back"));
		
		// if there is an error, print it and avoid to print the rest because probably
		// it won't be visible, the error indeed comes from the ejb class which retreives the tuples
		if(!errorMessage.equals("")) {
			out.println("Error, " + errorMessage );
			out.println("</body>");
			out.println("</html>");
			return;
		}
		
		// print the first materialized view, Number of total purchases per package

		out.println("<h3>Number of total purchases per package</h3> <br>");
		
		out.println("<ul>");
		for(MaterializedView1 mv : mv1) {
			out.print("<li>");
			out.println(mv.toString());
			out.print("</li>");
		}
		out.println("</ul>");
		
		// print the second materialized view, Number of total purchases per package and validity period

		out.println("<h3> Number of total purchases per package and validity period</h3> <br>");
				
		out.println("<ul>");
		for(MaterializedView2 mv : mv2) {
			out.print("<li>");
			out.println(mv.toString());
			out.print("</li>");
		}
		out.println("</ul>");
		
		// print the third materialized view, Total value of sales per package with and without the optional products

		out.println("<h3>  Total value of sales per package with and without the optional products</h3> <br>");
						
		out.println("<ul>");
		for(MaterializedView3 mv : mv3) {
			out.print("<li>");
			out.println(mv.toString());
			out.print("</li>");
		}
		out.println("</ul>");
				
		// print the fourth materialized view,  Average number of optional products sold together with each service package

		out.println("<h3> Average number of optional products sold together with each service package</h3> <br>");
								
		out.println("<ul>");
		for(MaterializedView4 mv : mv4) {
			out.print("<li>");
			out.println(mv.toString());
			out.print("</li>");
		}
		out.println("</ul>");
		
		// print the fifth materialized view first part,  List of insolvent users
		
		out.println("<h3> List of insolvent users</h3> <br>");
		
		out.println("<ul>");
		for(MaterializedView5Insolvents mv : mv5Insolvents) {
			out.print("<li>");
			out.println(mv.toString());
			out.print("</li>");
		}
		out.println("</ul>");
		
		
		// print the fifth materialized view second part,  List of suspended orders
		
		out.println("<h3> List of suspended orders</h3> <br>");
		
		out.println("<ul>");
		for(MaterializedView5Suspended mv : mv5Suspended) {
			out.print("<li>");
			out.println(mv.toString());
			out.print("</li>");
		}
		out.println("</ul>");
		
		// print the fifth materialized view third part,  List of alerts
		
		out.println("<h3> List of alerts</h3> <br>");
		
		out.println("<ul>");
		for(AuditingTable a : alerts) {
			out.print("<li>");
			out.println(a.toString());
			out.print("</li>");
		}
		out.println("</ul>");
		
		
		// print the sixth materialized view, best seller optional product
		
		if(bestSeller != null) {
			out.println("<h3> Best seller optional product</h3> <br>");
			out.println(bestSeller.toString());	
		}
		

		
		
		
		out.println("</body>");
		out.println("</html>");
	}
	
	
	
}
		
	


