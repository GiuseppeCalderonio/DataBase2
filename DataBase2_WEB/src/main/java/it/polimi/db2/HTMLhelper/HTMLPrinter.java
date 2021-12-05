package it.polimi.db2.HTMLhelper;

import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import it.polimi.db2.entities.*;
import it.polimi.db2.entities.Package;
import it.polimi.db2.Creation;

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
		
		out.println("<h1>Welcome to the Service Teleco Application</h1>");
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
	
	public void printHomePage(String homePageError, List<Package> packages, String username) {
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
				formInstances.add(new FormInstance(op.toString() ,"checkbox",op.getName()));
				
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
		
		out.println("<br>Package chose details: " + packageChosen.toString());
		out.println("<br>");
		out.println("</body>");
		out.println("</body>");
		out.println("<ul>");
		
		// print all the optional products chosen
		
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
	
	public void printEmployeeHomePage(String errorMessage, String username, Creation toCreate) {
		printHeader();
		out.println("<body>");
		out.println("<h1>In this page you can create a package with the associated optional products or an optional product</h1>");
		
		out.println("Hi " + username + "<br>");
		String toShow = "LogOut";
		
		Form goToLogin = new Form("LoginServlet", "GET", "", null, toShow);
		out.println(goToLogin.toString() + "<br>");
		
		try {
		
			switch(toCreate) {
			
			case OPTIONALPRODUCT:
				out.println("<b> Optional product creation <br> </b>");
				
				List<FormInstance> optionalProductInstances = new ArrayList<>();
				
				optionalProductInstances.add(new FormInstance("Name", "text", "name", true));
				optionalProductInstances.add(new FormInstance("Fee", "float", "fee", true));
				
				Form optionalProductForm = new Form("GoToEmployeeHomePage", "POST", "", optionalProductInstances, "create");
				out.println(optionalProductForm.toString());
				//TODO check if fee is a number
				break;
			
			case PACKAGE:
				break;
				
			case MOBILEPHONE:
				out.println("<b> Mobile phone service creation <br> </b>");
				
				List<FormInstance> mobilePhoneInstances = new ArrayList<>();
				
				mobilePhoneInstances.add(new FormInstance("Minutes", "int", "minutes", true));
				mobilePhoneInstances.add(new FormInstance("SMS", "int", "sms", true));
				mobilePhoneInstances.add(new FormInstance("Fee extra SMS", "float", "sms", true));
				
				Form mobilePhoneForm = new Form("GoToEmployeeHomePage", "POST", "", mobilePhoneInstances, "create");
				out.println(mobilePhoneForm.toString());
				//TODO check are numbers
				break;
				
			case MOBILEINTERNET:
				out.println("<b> Mobile internet service creation <br> </b>");
				
				List<FormInstance> mobileInternetInstances = new ArrayList<>();
				
				mobileInternetInstances.add(new FormInstance("GB", "int", "GIGA", true));
				mobileInternetInstances.add(new FormInstance("Fee extra GB", "float", "fee extra GB", true));
				
				Form mobileInternetForm = new Form("GoToEmployeeHomePage", "POST", "", mobileInternetInstances, "create");
				out.println(mobileInternetForm.toString());
				//TODO check are numbers
				break;
				
			case FIXEDINTERNET:
				out.println("<b> Fixed internet service creation <br> </b>");
				
				List<FormInstance> fixedInternetInstances = new ArrayList<>();
				
				fixedInternetInstances.add(new FormInstance("GB", "int", "GIGA", true));
				fixedInternetInstances.add(new FormInstance("Fee extra GB", "float", "fee extra GB", true));
				
				Form fixedInternetForm = new Form("GoToEmployeeHomePage", "POST", "", fixedInternetInstances, "create");
				out.println(fixedInternetForm.toString());
				//TODO check are numbers
				break;
				
			default:
				break;
			}
		
		} catch (NullPointerException e) {
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
	

}
