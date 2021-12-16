package it.polimi.db2.entities.materializedViews;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the mv_query5_suspended database table.
 * 
 */
@Entity
@Table(name="mv_query5_suspended")
@NamedQuery(name="MaterializedView5Suspended.findAll", query="SELECT m FROM MaterializedView5Suspended m")
public class MaterializedView5Suspended implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int orderid;

	public MaterializedView5Suspended() {
	}

	public int getOrderid() {
		return this.orderid;
	}

	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}

	@Override
	public String toString() {
		return "[orderid=" + orderid + "]";
	}
	
	

}