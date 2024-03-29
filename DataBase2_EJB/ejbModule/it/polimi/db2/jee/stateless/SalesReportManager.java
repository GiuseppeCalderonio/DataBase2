package it.polimi.db2.jee.stateless;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.polimi.db2.entities.materializedViews.AuditingTable;
import it.polimi.db2.entities.materializedViews.MaterializedView1;
import it.polimi.db2.entities.materializedViews.MaterializedView2;
import it.polimi.db2.entities.materializedViews.MaterializedView3;
import it.polimi.db2.entities.materializedViews.MaterializedView4;
import it.polimi.db2.entities.materializedViews.MaterializedView5Insolvents;
import it.polimi.db2.entities.materializedViews.MaterializedView5Suspended;
import it.polimi.db2.entities.materializedViews.MaterializedView6;

/**
 * Session Bean implementation class SalesReportManager
 */
@Stateless
public class SalesReportManager {
	
	@PersistenceContext(unitName = "DataBase2_EJB")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public SalesReportManager() {
        // TODO Auto-generated constructor stub
    }
    
    public List<AuditingTable> getAlerts(){
    	
    	List<AuditingTable> alerts = null;
		try {
			
			alerts = em.createNamedQuery("AuditingTable.findAll", AuditingTable.class).setHint("javax.persistence.cache.storeMode", "REFRESH").getResultList();
			
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new PersistenceException("Could not execute the query");
		}
		return alerts;
    }
    
    public List<MaterializedView1> getMaterializedView1(){
    	
    	List<MaterializedView1> materializedViewTuples = null;
		try {
			
			// refresh the state from the db
			materializedViewTuples = em.createNamedQuery("MaterializedView1.findAll", MaterializedView1.class).setHint("javax.persistence.cache.storeMode", "REFRESH").getResultList();
			
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new PersistenceException("Could not execute the query");
		}
		return materializedViewTuples;
    }
    
    public List<MaterializedView2> getMaterializedView2(){
    	
    	List<MaterializedView2> materializedViewTuples = null;
		try {
			materializedViewTuples = em.createNamedQuery("MaterializedView2.findAll", MaterializedView2.class).setHint("javax.persistence.cache.storeMode", "REFRESH").getResultList();
			
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new PersistenceException("Could not execute the query");
		}
		return materializedViewTuples;
    }
    
    public List<MaterializedView3> getMaterializedView3(){
	
		List<MaterializedView3> materializedViewTuples = null;
		try {
			materializedViewTuples = em.createNamedQuery("MaterializedView3.findAll", MaterializedView3.class).setHint("javax.persistence.cache.storeMode", "REFRESH").getResultList();
			
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new PersistenceException("Could not execute the query");
		}
		return materializedViewTuples;
	}

	public List<MaterializedView4> getMaterializedView4(){
		
		List<MaterializedView4> materializedViewTuples = null;
		try {
			materializedViewTuples = em.createNamedQuery("MaterializedView4.findAll", MaterializedView4.class).setHint("javax.persistence.cache.storeMode", "REFRESH").getResultList();
			
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new PersistenceException("Could not execute the query");
		}
		return materializedViewTuples;
	}

	public MaterializedView6 getBestSellerOptionalProduct(){
		
		MaterializedView6 bestSeller = null;
		try {
			bestSeller = em.createNamedQuery("getBestSeller", MaterializedView6.class).setHint("javax.persistence.cache.storeMode", "REFRESH").setMaxResults(1).getSingleResult();
			
			//em.refresh(bestSeller);
			
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new PersistenceException("Could not execute the query");
		}
		
		return bestSeller;
		
	}
	
	public List<MaterializedView5Insolvents> getMaterializedView5Insolvents(){
		
		List<MaterializedView5Insolvents> materializedViewTuples = null;
		try {
			materializedViewTuples = em.createNamedQuery("MaterializedView5Insolvents.findAll", MaterializedView5Insolvents.class).setHint("javax.persistence.cache.storeMode", "REFRESH").getResultList();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new PersistenceException("Could not execute the query");
		}
		return materializedViewTuples;
	}
	
	public List<MaterializedView5Suspended> getMaterializedView5Suspended(){
		
		List<MaterializedView5Suspended> materializedViewTuples = null;
		try {
			materializedViewTuples = em.createNamedQuery("MaterializedView5Suspended.findAll", MaterializedView5Suspended.class).setHint("javax.persistence.cache.storeMode", "REFRESH").getResultList();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new PersistenceException("Could not execute the query");
		}
		return materializedViewTuples;
	}



}
