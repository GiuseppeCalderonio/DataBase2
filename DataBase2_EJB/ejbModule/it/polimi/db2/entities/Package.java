package it.polimi.db2.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import it.polimi.db2.Service;

@Entity
@Table(name = "package")
@NamedQuery(name = "getPackages",
query = "SELECT p FROM Package p")
public class Package implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int packageid;
	
	private String name;
	
	private int fee12;
	
	private int fee24;
	
	private int fee36;
	
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

	public int getFee12() {
		return fee12;
	}

	public void setFee12(int fee12) {
		this.fee12 = fee12;
	}
	
	public int getFee24() {
		return fee24;
	}

	public void setFee24(int fee24) {
		this.fee24 = fee24;
	}
	
	public int getFee36() {
		return fee36;
	}

	public void setFee36(int fee36) {
		this.fee36 = fee36;
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
	
	public String toString() {
		
		// general info
		
		String toReturn = "Name = " + name //print name
				+ ", ID = " + packageid  //print id
				+ ", Montly fee for 12 months " + fee12 + "$ " //print m fee for 12 months
				+ ", Montly fee for 24 months " + fee24 + "$ " //print m fee for 24 months
				+ ", Montly fee for 36 months " + fee36 + "$ " //print m fee for 36 months
				+ "<br>";
		
		// print services offered
		
		toReturn = toReturn + "Services offered :";
		
		// gather services
		
		List<Service> servicesOffered = new ArrayList<>();
		servicesOffered.addAll(fixedPhoneServices);
		servicesOffered.addAll(internetServices);
		servicesOffered.addAll(mobilePhoneServices);
		
		// print a list of services
		
		toReturn = toReturn + "<ul>";
		
		for(Service s : servicesOffered) {
			toReturn = toReturn + "<li>" + s.toString() + "</li>";
		}
		
		toReturn = toReturn + "</ul>";
		
		// print optional products
		
		toReturn = toReturn + "<br> Optional Products: ";
		
		// if there are no optional products
		
		if(optionalProducts.isEmpty())
			return toReturn + "No";
		
		// print optional products
		
		toReturn = toReturn + "<ul>";
		
		for(OptionalProduct op : optionalProducts) {
			toReturn = toReturn + "<li>" + op.toString() + "</li>";
		}
		
		return toReturn + "</ul>";
	}
}
