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
import javax.persistence.ManyToOne;
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
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fixed_phone_id")
	private FixedPhone fixedPhoneService;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mobile_phone_id")
	private MobilePhone mobilePhoneService;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fixed_internet_id")
	private FixedInternet fixedInternetService;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mobile_internet_id")
	private MobileInternet mobileInternetService;
	
	// methods

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
	
	public void setOptionalProducts(List<OptionalProduct> optionalProducts) {
		this.optionalProducts = optionalProducts;
		for(OptionalProduct op : optionalProducts) {
			op.addPackage(this);
		}
	}

	public FixedPhone getFixedPhoneService() {
		return fixedPhoneService;
	}

	public void setFixedPhoneService(FixedPhone fixedPhoneService) {
		this.fixedPhoneService = fixedPhoneService;
		if(fixedPhoneService != null)
			fixedPhoneService.addPackage(this);
	}

	public MobilePhone getMobilePhoneServices() {
		return mobilePhoneService;
	}

	public void setMobilePhoneService(MobilePhone mobilePhoneService) {
		this.mobilePhoneService = mobilePhoneService;
		if(mobilePhoneService != null)
		mobilePhoneService.addPackage(this);
	}

	public FixedInternet getFixedInternetServices() {
		return fixedInternetService;
	}

	public void setFixedInternetService(FixedInternet fixedInternetService) {
		this.fixedInternetService = fixedInternetService;
		if(fixedInternetService != null)
		fixedInternetService.addPackage(this);
	}
	
	public MobileInternet getMobileInternetServices() {
		return mobileInternetService;
	}

	public void setMobileInternetService(MobileInternet mobileInternetService) {
		this.mobileInternetService = mobileInternetService;
		if(mobileInternetService != null)
		mobileInternetService.addPackage(this);
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
		if(fixedPhoneService != null)
			servicesOffered.add(fixedPhoneService);
		if(mobileInternetService != null)
			servicesOffered.add(mobileInternetService);
		if(fixedInternetService != null)
			servicesOffered.add(fixedInternetService);
		if(mobilePhoneService != null)
			servicesOffered.add(mobilePhoneService);
		
		//servicesOffered.stream().filter(so -> so != null).toList();
		
		//System.out.println(servicesOffered);
		
		if(servicesOffered.isEmpty())
			return toReturn + "No <br>";
		
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
	
	public int getFee(int validityPeriod) {
		if(validityPeriod == 12)
			return fee12;
		if (validityPeriod == 24)
			return fee24;
		return fee36;
	}
}
