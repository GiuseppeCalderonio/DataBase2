package it.polimi.db2.entities.materializedViews;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the mv_query1 database table.
 * 
 */
@Entity
@Table(name="mv_query1")
@NamedQuery(name="MaterializedView1.findAll", query="SELECT m FROM MaterializedView1 m")
public class MaterializedView1 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int packageid;

	private int purchase;

	public MaterializedView1() {
	}

	public int getPackageid() {
		return this.packageid;
	}

	public void setPackageid(int packageid) {
		this.packageid = packageid;
	}

	public int getPurchase() {
		return this.purchase;
	}

	public void setPurchase(int purchase) {
		this.purchase = purchase;
	}

	@Override
	public String toString() {
		return "[packageid= " + packageid + ", purchase= " + purchase + "]";
	}
	
	

}