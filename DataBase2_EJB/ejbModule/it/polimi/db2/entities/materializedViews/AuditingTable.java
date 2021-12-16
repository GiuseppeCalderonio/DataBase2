package it.polimi.db2.entities.materializedViews;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: AuditingTable
 *
 */
@Entity
@Table(name = "auditing_table")
@NamedQuery(name="AuditingTable.findAll", query="SELECT a FROM AuditingTable a")
public class AuditingTable implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	private int userid;
	
	
	private String username;
	
	private String email;
	
	private int amount;
	
	private Timestamp date;

	
	public AuditingTable() {
		super();
	}
	

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




	public String getEmail() {
		return email;
	}




	public void setEmail(String email) {
		this.email = email;
	}




	public int getAmount() {
		return amount;
	}




	public void setAmount(int amount) {
		this.amount = amount;
	}




	public Timestamp getDate() {
		return date;
	}




	public void setDate(Timestamp date) {
		this.date = date;
	}


	@Override
	public String toString() {
		return "[userid= " + userid + ", username= " + username + ", email= " + email + ", amount= " + amount
				+ ", date= " + date + "]";
	}


	

	
	
	
   
}
