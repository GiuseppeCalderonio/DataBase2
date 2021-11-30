package it.polimi.db2.HTMLhelper;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
		
		formInstances.add(new FormInstance("Username","text","username"));
		formInstances.add(new FormInstance("Email", "email", "email"));
		formInstances.add(new FormInstance("Password", "password", "password"));
		
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
	
	private void printHeader() {
		out.println("<!DOCTYPE html>");
		out.println("<html xmlns:th=\"http://www.thymeleaf.org\">");
		out.println("<head>");
		out.println("<meta charset=\"ISO-8859-1\">");
		out.println("<title>" + title + "</title>");
		out.println("</head>");
	}
	

}
