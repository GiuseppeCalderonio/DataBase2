package it.polimi.db2.entities.materializedViews;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the mv_query4 database table.
 * 
 */
@Entity
@Table(name="mv_query4")
@NamedQuery(name="MaterializedView4.findAll", query="SELECT m FROM MaterializedView4 m")
public class MaterializedView4 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int packageid;

	@Column(name="average_op")
	private float averageOp;

	public MaterializedView4() {
	}

	public int getPackageid() {
		return this.packageid;
	}

	public void setPackageid(int packageid) {
		this.packageid = packageid;
	}

	public float getAverageOp() {
		return this.averageOp;
	}

	public void setAverageOp(float averageOp) {
		this.averageOp = averageOp;
	}

	@Override
	public String toString() {
		return "[packageid= " + packageid + ", averageOp= " + averageOp + "]";
	}
	
	

}