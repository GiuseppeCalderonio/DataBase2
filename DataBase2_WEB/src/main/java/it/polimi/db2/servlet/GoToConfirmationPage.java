package it.polimi.db2.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import it.polimi.db2.HTMLhelper.HTMLPrinter;
import it.polimi.db2.entities.OptionalProduct;
import it.polimi.db2.entities.Package;

/**
 * Servlet implementation class GoToConfirmationPage
 */
@WebServlet("/GoToConfirmationPage")
public class GoToConfirmationPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
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
		
		Package packageChosen = null;
		int fee = 0;
		List<OptionalProduct> optionalProductsChosen = null;
		Date startDate = null;
		
		
		
		printPage("", response.getWriter(), packageChosen, fee, optionalProductsChosen, startDate);
		
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
	private void printPage(String errorMessage, @NotNull PrintWriter out, Package packageChosen, int fee,
			List<OptionalProduct> optionalProductsChosen, Date startDate) {
		
		new HTMLPrinter(out, "ConfirmationPage").printConfirmationPage(errorMessage, optionalProductsChosen, packageChosen, startDate, fee);
		
	}

}
