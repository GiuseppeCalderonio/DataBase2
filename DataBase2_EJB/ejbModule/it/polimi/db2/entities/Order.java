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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
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
@Table(name = "db2project.order")
@NamedQuery(name = "getInsolventOrdersOf",
query = "SELECT o.orderid FROM Order o WHERE o.isValid = false AND o.user = :user")
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
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userid")
	private User user;
	
	/**
	 * this attribute represents the package associated with the order,
	 * it has an eager fetch type because it is just one instance,
	 * it has a persist cascade because, when an order is created,
	 * it also upload the package adding it to its list of orders that use it,
	 *  so it should be updated as well  
	 */
	@ManyToOne(fetch = FetchType.EAGER)
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
	
	@Column(name = "validity_period")
	private int validityPeriod;
	
	private int fee;
	
	private int package_fee;
	
	/**
	 * this attribute represents the number of optional products chosen among all the 
	 * ones available from the service package purchased 
	 * basically optional_chosen = optionalProducts.size();
	 */
	private int optional_chosen;
	
	/**
	 * this attribute represents the optional products associated with the package,
	 * it has an eager fetch type because in the majority of cases, fetching an order
	 * implies to fetch all the details too, and also because it is assumed to have
	 * a small number of optional products
	 */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="choose",
	joinColumns=@JoinColumn(name="orderid"),
	inverseJoinColumns=@JoinColumn(name="name"))
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

	public Package getPackage() {
		return pack;
	}
	
	public void setPackage(Package pack) {
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

	public int getValidityPeriod() {
		return validityPeriod;
	}

	public void setValidityPeriod(int validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public int getPackage_fee() {
		return package_fee;
	}

	public void setPackage_fee(int package_fee) {
		this.package_fee = package_fee;
	}

	public Collection<OptionalProduct> getOptionalProducts() {
		return optionalProducts;
	}

	public void setOptionalProducts(Collection<OptionalProduct> optionalProducts) {
		this.optionalProducts = optionalProducts;
		this.optional_chosen = optionalProducts.size();
		for(OptionalProduct op : optionalProducts) {
			op.addOrder(this);
		}
	}
	
	/**
	 * this method returns the total amount of money provided by this order
	 * computed as (package fee[current validity period] * number of months) + (optional products fee * validity period)
	 * @return the total value of the order in terms of money
	 */
	public int getTotalValue() {
		int totalValue = 0;
		totalValue = totalValue + fee;
		for(OptionalProduct op : optionalProducts) {
			totalValue = totalValue + op.getFee();
		}
		
		return totalValue * validityPeriod;
	}

	@Override
	public String toString() {
		return "Order [orderid=" + orderid + ", user=" + user + ", pack=" + pack + ", date=" + date + ", startDate="
				+ startDate + ", time=" + time + ", isValid=" + isValid + ", validityPeriod=" + validityPeriod
				+ ", fee=" + fee + ", optionalProducts=" + optionalProducts + "]";
	}
	
	

}
