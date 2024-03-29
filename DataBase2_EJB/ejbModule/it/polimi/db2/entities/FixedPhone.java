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
@Table(name = "fixed_phone")
@NamedQuery(name = "getFixedPhoneServices",
query = "SELECT fp FROM FixedPhone fp")
public class FixedPhone implements Serializable, Service{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int serviceid;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "fixedPhoneService")
	private Collection<Package> packages;
	
	
	
	public int getServiceid() {
		return serviceid;
	}

	public void setServiceid(int serviceid) {
		this.serviceid = serviceid;
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
		String toReturn = "Fixed Phone: ID = " + serviceid;
		
		
		
		return toReturn;
	}

}
