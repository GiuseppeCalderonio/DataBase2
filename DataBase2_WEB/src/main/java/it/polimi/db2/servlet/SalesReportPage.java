package it.polimi.db2.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.db2.HTMLhelper.HTMLPrinter;
import it.polimi.db2.entities.FixedInternet;
import it.polimi.db2.entities.FixedPhone;
import it.polimi.db2.entities.MobileInternet;
import it.polimi.db2.entities.MobilePhone;
import it.polimi.db2.entities.OptionalProduct;
import it.polimi.db2.entities.materializedViews.*;
import it.polimi.db2.jee.stateless.UserManager;
import it.polimi.db2.jee.stateless.SalesReportManager;


/**
 * Servlet implementation class SalesReportPage
 */
@WebServlet("/SalesReportPage")
public class SalesReportPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name = "it.polimi.db2.jee.stateless/UserManager")
	private UserManager userManager;
	
	@EJB(name = "it.polimi.db2.jee.stateless/SalesReportManager")
	private SalesReportManager salesReportManager;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SalesReportPage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int userId;
		String username;

		
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
		
		getReportData(response.getWriter());
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private void getReportData(PrintWriter out) {
		String errorMessage = "";
		List<MaterializedView1> mv1 = new ArrayList<>();
		List<MaterializedView2> mv2 = new ArrayList<>();
		List<MaterializedView3> mv3 = new ArrayList<>();
		List<MaterializedView4> mv4 = new ArrayList<>();
		MaterializedView6 bestSeller = null;;
		List<MaterializedView5Insolvents> mv5Insolvents = new ArrayList<>();
		List<MaterializedView5Suspended> mv5Suspended = new ArrayList<>();
		List<AuditingTable> alerts = new ArrayList<>();
		
		try {
			
			mv1 = salesReportManager.getMaterializedView1();
			mv2 = salesReportManager.getMaterializedView2();
			mv3 = salesReportManager.getMaterializedView3();
			mv4 = salesReportManager.getMaterializedView4();
			bestSeller = salesReportManager.getBestSellerOptionalProduct();
			alerts = salesReportManager.getAlerts();
			mv5Insolvents = salesReportManager.getMaterializedView5Insolvents();
			mv5Suspended = salesReportManager.getMaterializedView5Suspended();
			
		}catch (PersistenceException e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
		}
		
		printPage(out, errorMessage, mv1, mv2, mv3, mv4, bestSeller, mv5Insolvents, mv5Suspended, alerts);
		
		
		
	}
	
	private void printPage(PrintWriter out, String errorMessage,
			List<MaterializedView1> mv1,
			List<MaterializedView2> mv2,
			List<MaterializedView3> mv3,
			List<MaterializedView4> mv4,
			MaterializedView6 bestSeller,
			List<MaterializedView5Insolvents> mv5Insolvents,
			List<MaterializedView5Suspended> mv5Suspended,
			List<AuditingTable> alerts) {
		
		
		new HTMLPrinter(out, "SalesReportPage").printSalesReportPage(errorMessage, mv1, mv2, mv3, mv4, bestSeller, mv5Insolvents, mv5Suspended, alerts);
		
	}

}
