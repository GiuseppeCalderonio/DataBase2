package it.polimi.db2.jee.stateless;

import java.util.List;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.polimi.db2.exceptions.PackagesNotFoundException;

import it.polimi.db2.entities.*;
import it.polimi.db2.entities.Package;

@Stateless
public class PackageManager {
	
	@PersistenceContext(unitName = "DataBase2_EJB")
	private EntityManager em;
	
	public PackageManager() {
	}

	public List<Package> getPackages() throws PackagesNotFoundException {
		List<Package> packagesList = null;
		try {
			packagesList = em.createNamedQuery("getPackages", Package.class).getResultList();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new PackagesNotFoundException("Could not retrieve packages");
		}
		if (packagesList.isEmpty())
			return null;
		else {
		
			return packagesList;
		}
	
	}
	
	public Package getPackage(int packageId){
		Package found = em.find(Package.class, packageId);
		return found;
	}


}
