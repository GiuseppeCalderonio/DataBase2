package it.polimi.db2.HTMLhelper;

import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import it.polimi.db2.entities.*;
import it.polimi.db2.entities.Package;

public class HTMLPrinter {
	
	private PrintWriter out;
	private String title;
	
	public HTMLPrinter(PrintWriter out, String title) {
		this.out = out;
		this.title = title;
	}
	
	public void printLoginPage(String loginError, String registrationError) {
		
		List<FormInstance> loginInstances = new ArrayList<>();
		
		loginInstances.add(new FormInstance("Username", "text", "username"));
		loginInstances.add(new FormInstance("Password", "password", "pwd"));
		
		Form loginForm = new Form("LoginServlet", "POST", loginError, loginInstances, "login");
		
		List<FormInstance> formInstances = new ArrayList<>();
		
		formInstances.add(new FormInstance("Username","text","username", true));
		formInstances.add(new FormInstance("Email", "email", "email"));
		formInstances.add(new FormInstance("Password", "password", "password"));
		formInstances.add(new FormInstance("Register as Employee","checkbox","isEmployee", false));
		
		Form registrationForm = new Form("RegistrationServlet", "POST", registrationError, formInstances, "registration");
		
		
		printHeader();
		out.println("<body>");
		out.println("<h1>Welcome to the Service Teleco Application</h1>");
		out.println(loginForm.toString());
		out.println("<h1> Registration Form</h1>");
		out.println(registrationForm.toString());
		
		out.println("</body>");
		out.println("</html>");
		
	}
	
	public void printHomePage(String homePageError, List<Package> packages, String username) {
		printHeader();
		out.println("<body>");
		out.println("<h1>here they are our packages, if you want to pursue an order press the button Buy Service </h1>");
		
		// user logged in
		
		if(username != null) 
			out.println("Hi " + username + "<br>");
		
		// user not logged in yet
		
		else {
			Form goToLogin = new Form("LoginServlet", "GET", "", null, "Login");
			out.println(goToLogin.toString());
		}
		
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
			
			formInstances.add(new FormInstance("Start date ","date","startDate"));
			
			for(OptionalProduct op : packageChosen.getOptionalProducts()) {
				formInstances.add(new FormInstance(op.toString() ,"checkbox",op.getName()));
				
			}
			
			Form buyPackage = new Form("GoToBuyService", "POST", "", formInstances, "Confirm Payment", validityPeriodOption);
			out.print(buyPackage.toString());
		}
		
		
		/*
		out.println("<body>");
		out.println("<body>");
		out.println("<body>");
		out.println("<body>");
		
		
		
		
		<form action="/action_page.php">
		  <label for="validityPeriod">Choose a validity period:</label>
		  <select name="validityPeriod" id="cars">
		    <option value="12">12 Months</option>
		    <option value="24">24 Months</option>
		    <option value="36"> 36 Months</option>
		  </select>
		
		
		
		for(Package p : packages) {
			availablePackages.add(new FormInstance(p.getName() + "<br>", "radio", "packageId"));
			availablePackages.add(new FormInstance());
		}
		
		Form buyPackage = new Form("GoToConfirmation", "GET", "", availablePackages, "Confirm Payment");
		out.print(buyPackage.toString());
		out.println("<br>");
		out.println(errorMessage);
		*/
		
		out.println("</body>");
		out.println("</html>");
	}
	
	public void printConfirmationPage(String errorMessage, List<OptionalProduct> optionalProductChosen, Package packageChosen, Date startdate, int fee) {
		printHeader();
		out.println("<body>");
		out.println("<h1> That's a recap of what you chose, check if all the data are correct </h1>");
		
		
		
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
	

}
