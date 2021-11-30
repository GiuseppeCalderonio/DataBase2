package it.polimi.db2.entities;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;


/**
 * this class represents the entity order, connected with user,
 *  package and optional product
 * @author giuca
 *
 */
@Entity
@Table(name = "order")
public class Order implements Serializable{
	
	/**
	 * this attribute represents the primary key of the entity
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "orderid")
	private int orderid;
	
	/**
	 * this attribute represents the user associated with the order,
	 * it has an eager fetch type because it is just one instance
	 * it has a persist cascade type because, when an order is created,
	 * it also upload the user adding it to his list of orders, so it should
	 * be updated as well
	 */
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "userid")
	private User user;
	
	/**
	 * this attribute represents the package associated with the order,
	 * it has an eager fetch type because it is just one instance,
	 * it has a persist cascade because, when an order is created,
	 * it also upload the package adding it to its list of orders that use it,
	 *  so it should be updated as well  
	 */
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "packageid")
	private Package pack;
	
	/**
	 * this attribute represents the date in which the order is created
	 */
	@Column(name = "date")
	private Date date;
	
	/**
	 * this attribute represents the date from which the service package starts to
	 * be valid
	 */
	@Column(name = "startdate")
	private Date startDate;
	
	/**
	 * this attribute represents the time in which the order is created
	 */
	@Column(name = "time")
	private Time time;
	
	/**
	 * this attribute represents whether the order is valid or not
	 */
	@Column(name = "isvalid")
	private boolean isValid;
	
	/**
	 * this attribute represents the optional products associated with the package,
	 * it has an eager fetch type because in the majority of cases, fetching an order
	 * implies to fetch all the details too, and also because it is assumed to have
	 * a small number of optional products
	 * it has a persist cascade type because, when an order is created, it updates also
	 * the collections of the optional products holding a reference to all the orders
	 * which use it
	 */
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "orders", cascade = CascadeType.PERSIST)
	private Collection<OptionalProduct> optionalProducts;

	public int getOrderid() {
		return orderid;
	}

	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}

	public User getUser() {
		return user;
	}

	/**
	 * this method set the user associated with the order
	 * it also updates the user adding this to the list of orders
	 * @param user this is the user who did the order and to update
	 */
	public void setUser(User user) {
		this.user = user;
		user.addOrder(this);
	}

	public Package getPack() {
		return pack;
	}
	
	public void setPack(Package pack) {
		this.pack = pack;
		pack.addOrder(this);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public Collection<OptionalProduct> getOptionalProducts() {
		return optionalProducts;
	}

	public void setOptionalProducts(Collection<OptionalProduct> optionalProducts) {
		this.optionalProducts = optionalProducts;
		for(OptionalProduct op : optionalProducts) {
			op.addOrder(this);
		}
	}

}
