package it.polimi.db2.entities.materializedViews;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the mv_query6 database table.
 * 
 */
@Entity
@Table(name="mv_query6")
@NamedQuery(name="getBestSeller",
query="SELECT bestseller FROM MaterializedView6 bestseller WHERE bestseller.revenue ="
		+ " (SELECT max(op.revenue) FROM MaterializedView6 op)")
public class MaterializedView6 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String name;

	
	private int revenue;

	public MaterializedView6() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRevenue() {
		return this.revenue;
	}

	public void setRevenue(int revenue) {
		this.revenue = revenue;
	}

	@Override
	public String toString() {
		return "[name=" + name + ", revenue=" + revenue + "]";
	}
	
	

}