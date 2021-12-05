package it.polimi.db2.entities;

import java.util.Collection;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

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

import it.polimi.db2.Service;

@Entity
@Table(name = "mobile_phone")
@NamedQuery(name = "getMobilePhoneServices",
query = "SELECT mp FROM MobilePhone mp")
public class MobilePhone implements Serializable, Service{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int serviceid;
	
	private int minutes;
	
	private int sms;
	
	@Column(name = "minutes_extra_fee")
	private int minutesExtraFee;
	
	@Column(name = "sms_extra_fee")
	private int smsExtraFee;
	
	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mobilePhoneService")
	private Collection<Package> packages;
	
	
	
	public int getServiceid() {
		return serviceid;
	}

	public void setServiceid(int serviceid) {
		this.serviceid = serviceid;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public int getSms() {
		return sms;
	}

	public void setSms(int sms) {
		this.sms = sms;
	}

	public int getMinutesExtraFee() {
		return minutesExtraFee;
	}

	public void setMinutesExtraFee(int minutesExtraFee) {
		this.minutesExtraFee = minutesExtraFee;
	}

	public int getSmsExtraFee() {
		return smsExtraFee;
	}

	public void setSmsExtraFee(int smsExtraFee) {
		this.smsExtraFee = smsExtraFee;
	}

	public void setPackages(Collection<Package> packages) {
		this.packages = packages;
	}

	public Collection<Package> getPackages() {
		return packages;
	}

	public void addPackage(Package pack) {
		packages.add(pack);
	}
	
	public String toString() {
		String toReturn = "Mobile Phone:"
				+ " ID = " + serviceid
				+ ", Minutes =  " + minutes
				+ ", SMSs = " + sms
				+ ", Extra fees for minute = " + minutesExtraFee
				+ ", Extra fees for SMS = " + smsExtraFee;
		
		
		
		return toReturn;
	}

}
