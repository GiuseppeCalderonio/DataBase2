package it.polimi.db2.entities.materializedViews;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the mv_query3 database table.
 * 
 */
@Entity
@Table(name="mv_query3")
@NamedQuery(name="MaterializedView3.findAll", query="SELECT m FROM MaterializedView3 m")
public class MaterializedView3 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int packageid;

	@Column(name="partial_sales")
	private int partialSales;

	@Column(name="total_sales")
	private int totalSales;

	public MaterializedView3() {
	}

	public int getPackageid() {
		return this.packageid;
	}

	public void setPackageid(int packageid) {
		this.packageid = packageid;
	}

	public int getPartialSales() {
		return this.partialSales;
	}

	public void setPartialSales(int partialSales) {
		this.partialSales = partialSales;
	}

	public int getTotalSales() {
		return this.totalSales;
	}

	public void setTotalSales(int totalSales) {
		this.totalSales = totalSales;
	}

	@Override
	public String toString() {
		return "[packageid= " + packageid + ", partialSales= " + partialSales + ", totalSales= "
				+ totalSales + "]";
	}
	
	

}