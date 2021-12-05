package it.polimi.db2.jee.stateless;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.polimi.db2.entities.FixedPhone;
import it.polimi.db2.entities.Internet;
import it.polimi.db2.entities.MobilePhone;
import it.polimi.db2.entities.OptionalProduct;
import it.polimi.db2.entities.Package;
import it.polimi.db2.exceptions.CredentialsException;
import it.polimi.db2.exceptions.OptinalProductsNotFoundException;
import it.polimi.db2.exceptions.PackagesNotFoundException;
import it.polimi.db2.exceptions.ServiceNotFoundException;

@Stateless
public class EmployeeManager {
	
	@PersistenceContext(unitName = "DataBase2_EJB")
	private EntityManager em;
	
	public List<OptionalProduct> getOptionalProducts() throws OptinalProductsNotFoundException {
		List<OptionalProduct> optionalProducts = null;
		try {
			optionalProducts = em.createNamedQuery("getOptionalProducts", OptionalProduct.class).getResultList();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new OptinalProductsNotFoundException("Could not retrieve packages");
		}
		if (optionalProducts.isEmpty())
			return null;
		else {
		
			return optionalProducts;
		}
	
	}
	
	public List<Internet> getInternetServices() throws ServiceNotFoundException {
		List<Internet> internetServices = null;
		try {
			internetServices = em.createNamedQuery("getInternetServices", Internet.class).getResultList();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new ServiceNotFoundException("Could not retrieve packages");
		}
		return internetServices;
	
	}
	
	public List<FixedPhone> getFixedPhoneServices() throws ServiceNotFoundException {
		List<FixedPhone> fixedPhoneServices = null;
		try {
			fixedPhoneServices = em.createNamedQuery("getFixedPhoneServices", FixedPhone.class).getResultList();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new ServiceNotFoundException("Could not retrieve packages");
		}
		return fixedPhoneServices;
	
	}
	
	public List<MobilePhone> getMobilePhoneServices() throws ServiceNotFoundException {
		List<MobilePhone> mobilePhoneServices = null;
		try {
			mobilePhoneServices = em.createNamedQuery("getMobilePhoneServices", MobilePhone.class).getResultList();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new ServiceNotFoundException("Could not retrieve packages");
		}
		
		return mobilePhoneServices;
	
	}
	
	public void addPackage(String name,
			List<OptionalProduct> optionalProducts,
			Internet i,
			FixedPhone fp,
			MobilePhone mp,
			int fee12,
			int fee24,
			int fee36) throws PersistenceException {
		Package pack = new Package();
		pack.setName(name);
		pack.setOptionalProducts(optionalProducts);
		pack.setFixedPhoneService(fp);
		pack.setMobilePhoneService(mp);
		pack.setInternetService(i);
		pack.setFee12(fee12);
		pack.setFee24(fee24);
		pack.setFee36(fee36);
		
		try {
			em.persist(pack);
		}catch(PersistenceException e) {
			throw new PersistenceException("Package persist failed");
		}
		
	}
	
	public void addOptionalProduct(String name, int fee) throws PersistenceException {
		
		OptionalProduct op = new OptionalProduct();
		op.setName(name);
		op.setFee(fee);
		
		try {
			em.persist(op);
		}catch(PersistenceException e) {
			throw new PersistenceException("Optional Product persist failed");
		}
		
	}
	
	public void addFixedPhoneService() throws PersistenceException {
		
		FixedPhone fp = new FixedPhone();
		
		try {
			em.persist(fp);
		}catch(PersistenceException e) {
			throw new PersistenceException("fixed phone service persist failed");
		}
	}
	
	public void addMobilePhoneService(int minutes, int sms, int minutesFee, int smsFee) throws PersistenceException {
		MobilePhone mp = new MobilePhone();
		
		mp.setMinutes(minutes);
		mp.setSms(sms);
		mp.setMinutesExtraFee(minutesFee);
		mp.setSmsExtraFee(smsFee);
		
		try {
			em.persist(mp);
		}catch(PersistenceException e) {
			throw new PersistenceException("mobile phone service persist failed");
		}
	}
	
	public void addInternetService(String name, int GB, int GBFee) throws PersistenceException  {
		Internet i = new Internet();
		
		i.setName(name);
		i.setGigaBytes(GB);
		i.setGigaBytesExtraFee(GBFee);
		
		try {
			em.persist(i);
		}catch(PersistenceException e) {
			throw new PersistenceException(name + " internet service persist failed");
		}
		
	}

}
