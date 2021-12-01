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

import org.apache.commons.lang3.StringEscapeUtils;

import it.polimi.db2.HTMLhelper.HTMLPrinter;
import it.polimi.db2.entities.OptionalProduct;
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
			 packages = packageManager.getPackages();
		} catch (PackagesNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Package packageChosen;
		
		try {
			packageChosen = packageManager.getPackage(Integer.parseInt(request.getParameter("packageid")));
			request.getSession().setAttribute("packageid", packageChosen.getPackageid());
		}catch(NumberFormatException e) {
			//e.printStackTrace();
			packageChosen = null;
		}
		
		printPage("", response.getWriter(), packages, packageChosen);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("POST\n");
		String validityPeriod = StringEscapeUtils.escapeJava(request.getParameter("validityPeriod"));
		// store the validity period chosen
		request.getSession().setAttribute("validityPeriod", Integer.parseInt(validityPeriod));
		
		
		// store the start date chosen
		System.out.println(request.getParameter("startDate"));
		request.getSession().setAttribute("startDate", request.getAttribute("startDate"));
		Package packageChosen = packageManager.getPackage((int)request.getSession().getAttribute("packageid"));
		
		List<String> optionalProductsChosen = packageChosen.getOptionalProducts()
				.stream().map(op -> op.getName()).toList();
		
		optionalProductsChosen.stream().filter(op -> request.getParameter(op) != null).toList();
		
		
		String path = getServletContext().getContextPath() + "/GoToConfirmationPage";
		response.sendRedirect(path);
	}

	
	private void printPage(String errorMessage, @NotNull PrintWriter out, List<Package> packages, Package packageChosen) {
		
		new HTMLPrinter(out, "BuyServicePage").printBuyServicePage(errorMessage, packages, packageChosen);
	}
	
}
