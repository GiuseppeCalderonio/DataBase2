package it.polimi.db2.jee.stateless;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.polimi.db2.entities.*;
import it.polimi.db2.entities.Package;
import it.polimi.db2.exceptions.CredentialsException;
import it.polimi.db2.exceptions.OrderException;


@Stateless
public class OrderManager {
	
	@PersistenceContext(unitName = "DataBase2_EJB")
	private EntityManager em;
	
	public void createOrder(User user,
			Package pack,
			List<OptionalProduct> optionalProducts,
			Date startDate,
			boolean isValid,
			int validityPeriod) throws ParseException, OrderException {
		
		Order order = new Order();
		order.setUser(user);
		order.setPackage(pack);
		order.setOptionalProducts(optionalProducts);
		order.setDate(getTodayDate());
		order.setStartDate(startDate);
		order.setTime(getTodayTime());
		order.setValid(isValid);
		order.setValidityPeriod(validityPeriod);
		order.setFee(getFee(pack, validityPeriod));
		
		
		
		
		try {
			em.persist(order);
		}catch(PersistenceException e) {
			
			e.printStackTrace();
			
			throw new OrderException("The order was not created correctly");
		}
		
		
	}
	
	public void updateOrder(int orderId, boolean isValid) {
		Order toModify = em.find(Order.class, orderId);
		toModify.setValid(isValid);
	}
	
	public Order getOrder(int orderId) {
		return em.find(Order.class, orderId);
	}
	
	private Date getTodayDate() throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		return new java.sql.Date(new java.util.Date().getTime());
		
	}
	
	private Time getTodayTime() {
		return new Time(System.currentTimeMillis());
	}
	
	private int getFee(Package pack, int validityPeriod) {
		return pack.getFee(validityPeriod);
	}

}
