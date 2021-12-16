package it.polimi.db2.entities.materializedViews;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the mv_query2 database table.
 * 
 */

public class MaterializedView2PK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	
	private int packageid;

	
	private int validityPeriod;

	public MaterializedView2PK() {
	}
	public int getPackageid() {
		return this.packageid;
	}
	public void setPackageid(int packageid) {
		this.packageid = packageid;
	}
	public int getValidityPeriod() {
		return this.validityPeriod;
	}
	public void setValidityPeriod(int validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MaterializedView2PK)) {
			return false;
		}
		MaterializedView2PK castOther = (MaterializedView2PK)other;
		return 
			(this.packageid == castOther.packageid)
			&& (this.validityPeriod == castOther.validityPeriod);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.packageid;
		hash = hash * prime + this.validityPeriod;
		
		return hash;
	}
	@Override
	public String toString() {
		return "packageid= " + packageid + ", validityPeriod= " + validityPeriod;
	}
	
	
}