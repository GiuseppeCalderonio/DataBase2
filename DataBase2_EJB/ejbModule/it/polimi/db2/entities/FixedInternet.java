package it.polimi.db2.entities;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
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

import it.polimi.db2.Service;

@Entity
@Table(name = "fixed_internet")
@NamedQuery(name = "getFixedInternetServices",
query = "SELECT i FROM FixedInternet i")
public class FixedInternet implements Serializable, Service{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int serviceid;
	
	@Column(name = "gb")
	private int gigaBytes;
	
	@Column(name = "gb_extra_fee")
	private int gigaBytesExtraFee;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "fixedInternetService")
	private Collection<Package> packages;

	public int getServiceid() {
		return serviceid;
	}

	public void setServiceid(int serviceid) {
		this.serviceid = serviceid;
	}

	public int getGigaBytes() {
		return gigaBytes;
	}

	public void setGigaBytes(int gigaBytes) {
		this.gigaBytes = gigaBytes;
	}

	public int getGigaBytesExtraFee() {
		return gigaBytesExtraFee;
	}

	public void setGigaBytesExtraFee(int gigaBytesExtraFee) {
		this.gigaBytesExtraFee = gigaBytesExtraFee;
	}

	public Collection<Package> getPackages() {
		return packages;
	}

	public void addPackage(Package pack) {
		if(packages == null) {
			packages = new ArrayList<>();
		}
		packages.add(pack);
	}
	
	public String toString() {
		String toReturn = "Fixed Internet:"
				+ " ID = " + serviceid
				+ ", GB =  " + gigaBytes
				+ ", Extra fees for GB = " + gigaBytesExtraFee;
		
		
		
		return toReturn;
	}

}
