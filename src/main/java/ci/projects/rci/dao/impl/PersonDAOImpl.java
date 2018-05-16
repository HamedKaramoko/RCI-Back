/**
 * 
 */
package ci.projects.rci.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import ci.projects.rci.dao.PersonDAO;
import ci.projects.rci.model.Person;

/**
 * @author hamedkaramoko
 *
 */
@Repository
public class PersonDAOImpl implements PersonDAO {
	
	@PersistenceContext
	private EntityManager em;


	public Person save(Person personToSave) {
		em.persist(personToSave);
		return personToSave;
	}

	public Person update(Person personToUpdate) {
		Person person = null;
		if(personToUpdate.getId() == null){
			return null;
		}
		person = get(personToUpdate.getId());
		if(person == null){
			return null;
		}
		return em.merge(personToUpdate);
	}

	public Person delete(long idPersonToDelete) {
		Person personToDelete = get(idPersonToDelete);
		if(personToDelete != null){
			em.remove(personToDelete);
		}
		return personToDelete;
	}

	public Person get(long id) {
		return em.find(Person.class, id);
	}
	
	public Person getByLogin(String login) {
		try {
			return em.createQuery("SELECT p FROM Person p WHERE p.login = :login", Person.class).setParameter("login", login).getSingleResult();
		}catch(NoResultException nRE) {
			return null;
		}
		
	}

	public List<Person> getAll() {
		return em.createQuery("SELECT p FROM Person p", Person.class).getResultList();
	}

}
