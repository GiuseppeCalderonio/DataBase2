package it.polimi.db2.entities.materializedViews;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the mv_query2 database table.
 * 
 */
@Entity
@Table(name="mv_query2")
@NamedQuery(name="MaterializedView2.findAll", query="SELECT m FROM MaterializedView2 m")
@IdClass(MaterializedView2PK.class)
public class MaterializedView2 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int packageid;
	
	@Id
	@Column(name = "validity_period")
	private int validityPeriod;

	private int purchase;

	public MaterializedView2() {
	}

	
	

	public int getPackageid() {
		return packageid;
	}




	public void setPackageid(int packageid) {
		this.packageid = packageid;
	}




	public int getValidityPeriod() {
		return validityPeriod;
	}




	public void setValidityPeriod(int validityPeriod) {
		this.validityPeriod = validityPeriod;
	}




	public int getPurchase() {
		return this.purchase;
	}

	public void setPurchase(int purchase) {
		this.purchase = purchase;
	}




	@Override
	public String toString() {
		return "[packageid= " + packageid + ", validityPeriod= " + validityPeriod + ", purchase= "
				+ purchase + "]";
	}

	
	

}