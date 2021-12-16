package it.polimi.db2.entities.materializedViews;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: MaterializedView5Insolvents
 *
 */
@Entity
@Table(name = "mv_query5_insolvents")
@NamedQuery(name="MaterializedView5Insolvents.findAll", query="SELECT m FROM MaterializedView5Insolvents m")
public class MaterializedView5Insolvents implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	private int userid;
	
	
	
	

	public int getUserid() {
		return userid;
	}





	public void setUserid(int userid) {
		this.userid = userid;
	}

	public MaterializedView5Insolvents() {
		super();
	}





	@Override
	public String toString() {
		return "[userid=" + userid + "]";
	}
   
	
	
}
