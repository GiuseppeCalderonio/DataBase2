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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "optional_product")
public class OptionalProduct implements Serializable{
	
	@Id
	private String name;
	
	private int fee;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="choose",
	joinColumns=@JoinColumn(name="name"),
	inverseJoinColumns=@JoinColumn(name="orderid"))
	private Collection<Order> orders;
	
	@ManyToMany(fetch = FetchType.LAZY,
			mappedBy = "optionalProducts",
			cascade = CascadeType.PERSIST)
	private Collection<Package> packages;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public void addOrder(Order order) {
		orders.add(order);
	}

	public Collection<Package> getPackages() {
		return packages;
	}

	public void setPackages(Collection<Package> packages) {
		this.packages = packages;
		for(Package p : packages) {
			p.addOptionalProduct(this);
		}
	}
	
	/**
	 * this method is called when a new package is created and wants to
	 * include this optional product.
	 * In particular, it adds the package to the ones that include this,
	 * and add this optional product to the ones included by the package
	 * @param pack this parameter represents the package that will add 
	 * 		  this and that will be added to this
	 */
	public void addPackage(Package pack) {
		packages.add(pack);
		pack.addOptionalProduct(this);
	}
	
	
	

}
