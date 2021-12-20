package it.polimi.db2.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "user")
@NamedQuery(name = "User.checkCredentials",
query = "SELECT u FROM User u  WHERE u.username = ?1 and u.password = ?2")
public class User implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userid;
	
	private String username;
	
	private String password;
	
	private String email;
	
	@Column(name = "is_employee")
	private boolean isEmployee;
	
	@Column(name = "is_insolvent")
	private boolean isInsolvent;
	
	@Column(name = "insolvent_payments")
	private int failedPayments;
	
	@OneToMany(fetch = FetchType.EAGER,
			mappedBy = "user")
	@OrderBy("date DESC")
	private Collection<Order> orders;

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEmployee() {
		return isEmployee;
	}

	public void setEmployee(boolean isEmployee) {
		this.isEmployee = isEmployee;
	}

	public boolean isInsolvent() {
		return isInsolvent;
	}

	public void setInsolvent(boolean isInsolvent) {
		this.isInsolvent = isInsolvent;
	}

	public int getFailedPayments() {
		return failedPayments;
	}

	public void setFailedPayments(int failedPayments) {
		this.failedPayments = failedPayments;
	}

	public Collection<Order> getOrders() {
		return orders;
	}

	public void setOrders(Collection<Order> orders) {
		this.orders = orders;
	}
	
	public void addOrder(Order order) {
		this.orders.add(order);
	}
	
	

}
