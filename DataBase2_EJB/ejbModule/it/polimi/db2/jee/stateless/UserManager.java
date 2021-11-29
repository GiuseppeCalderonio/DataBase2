package it.polimi.db2.jee.stateless;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import it.polimi.db2.entities.User;
import it.polimi.db2.exceptions.CredentialsException;

@Stateless
public class UserManager {
	
	@PersistenceContext(unitName = "DataBase2_EJB")
	private EntityManager em;
	
	public UserManager() {
		
	}
	
	public User findById(int userid) {
		return em.find(User.class, userid);
	}
	
	public Integer checkCredentials(String usrn, String pwd) throws CredentialsException, NonUniqueResultException {
		List<User> usersList = null;
		try {
			usersList = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, usrn)
					.setParameter(2, pwd).getResultList();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new CredentialsException("Could not verify credentals");
		}
		if (usersList.isEmpty())
			return null;
		else if (usersList.size() == 1) {
			User found = usersList.get(0);
			return found.getUserid();
		}
		throw new NonUniqueResultException("More than one user registered with same credentials");

	}
	
	/**
	 * this method registers a new user if the database allows it,
	 * so when the strings sre not null and the username and password are both unique
	 * @param usrn this is the username to register
	 * @param pwd this is the password to register
	 * @param email this is the mail to register
	 * @throws CredentialsException
	 */
	public void registerUser(String usrn, String pwd, String email) throws CredentialsException {
		User user = new User();
		user.setUsername(usrn);
		user.setPassword(pwd);
		user.setEmail(email);
		
		try {
			em.persist(user);
		}catch(PersistenceException e) {
			throw new CredentialsException("Credential insert may be already used or not correct");
		}
	}

}
