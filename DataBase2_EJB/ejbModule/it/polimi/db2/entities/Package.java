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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "package")
public class Package implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int packageid;
	
	private String name;
	
	@Column( name="validity_period")
	private int validityPeriod;
	
	private int fee;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "pack")
	@OrderBy("date DESC")
	private Collection<Order> orders;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="includes",
	joinColumns=@JoinColumn(name="packageid"),
	inverseJoinColumns=@JoinColumn(name="name"))
	private Collection<OptionalProduct> optionalProducts;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(name="provides",
	joinColumns=@JoinColumn(name="packageid"),
	inverseJoinColumns=@JoinColumn(name="serviceid"))
	private Collection<FixedPhone> fixedPhoneServices;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(name="provides",
	joinColumns=@JoinColumn(name="packageid"),
	inverseJoinColumns=@JoinColumn(name="serviceid"))
	private Collection<MobilePhone> mobilePhoneServices;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(name="provides",
	joinColumns=@JoinColumn(name="packageid"),
	inverseJoinColumns=@JoinColumn(name="serviceid"))
	private Collection<Internet> internetServices;

	public int getPackageid() {
		return packageid;
	}

	public void setPackageid(int packageid) {
		this.packageid = packageid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Collection<Order> getOrders() {
		return orders;
	}

	public void setOrders(Collection<Order> orders) {
		this.orders = orders;
	}
	
	public void addOrder(Order order) {
		orders.add(order);
	}

	public Collection<OptionalProduct> getOptionalProducts() {
		return optionalProducts;
	}
	
	public void addOptionalProduct(OptionalProduct optionalProduct) {
		optionalProducts.add(optionalProduct);
	}

	public Collection<FixedPhone> getFixedPhoneServices() {
		return fixedPhoneServices;
	}

	public void addFixedPhoneServices(FixedPhone fixedPhoneService) {
		this.fixedPhoneServices.add(fixedPhoneService);
		fixedPhoneService.addPackage(this);
	}

	public Collection<MobilePhone> getMobilePhoneServices() {
		return mobilePhoneServices;
	}

	public void addMobilePhoneServices(MobilePhone mobilePhoneService) {
		this.mobilePhoneServices.add(mobilePhoneService);
		mobilePhoneService.addPackage(this);
	}

	public Collection<Internet> getInternetServices() {
		return internetServices;
	}

	public void addInternetServices(Internet internetService) {
		this.internetServices.add(internetService);
		internetService.addPackage(this);
	}

}
