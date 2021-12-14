package it.polimi.db2.jee.stateless;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.polimi.db2.entities.FixedPhone;
import it.polimi.db2.entities.MobileInternet;
import it.polimi.db2.entities.FixedInternet;
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
			throw new OptinalProductsNotFoundException("Could not retrieve optional products");
		}

		return optionalProducts;
	
	}
	
	public List<FixedInternet> getFixedInternetServices() throws ServiceNotFoundException {
		List<FixedInternet> internetServices = null;
		try {
			internetServices = em.createNamedQuery("getFixedInternetServices", FixedInternet.class).getResultList();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new ServiceNotFoundException("Could not retrieve packages");
		}
		return internetServices;
	
	}
	
	public List<MobileInternet> getMobileInternetServices() throws ServiceNotFoundException {
		List<MobileInternet> internetServices = null;
		try {
			internetServices = em.createNamedQuery("getMobileInternetServices", MobileInternet.class).getResultList();
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
			FixedInternet fi,
			MobileInternet mi,
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
		pack.setFixedInternetService(fi);
		pack.setMobileInternetService(mi);
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
	
	public void addFixedInternetService(int GB, int GBFee) throws PersistenceException  {
		FixedInternet i = new FixedInternet();
		
		i.setGigaBytes(GB);
		i.setGigaBytesExtraFee(GBFee);
		
		try {
			em.persist(i);
		}catch(PersistenceException e) {
			throw new PersistenceException("Fixed internet service persist failed");
		}
		
	}
	
	public void addMobileInternetService(int GB, int GBFee) throws PersistenceException  {
		MobileInternet i = new MobileInternet();
		
		i.setGigaBytes(GB);
		i.setGigaBytesExtraFee(GBFee);
		
		try {
			em.persist(i);
		}catch(PersistenceException e) {
			throw new PersistenceException("Mobile internet service persist failed");
		}
		
	}
	
	public MobilePhone getMobilePhone(int id) {
		return em.find(MobilePhone.class, id);
	}
	
	public MobileInternet getMobileInternet(int id) {
		return em.find(MobileInternet.class, id);
	}
	
	public FixedInternet getFixedInternet(int id) {
		return em.find(FixedInternet.class, id);
	}

}
