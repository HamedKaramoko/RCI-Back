/**
 * 
 */
package ci.projects.rci.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
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

	public Person update(Person personToSave) {
		return em.merge(personToSave);
	}

	public Person delete(long idPersonToDelete) {
		Person personToDelete = get(idPersonToDelete);
		em.remove(personToDelete);
		return personToDelete;
	}

	public Person get(long id) {
		return em.find(Person.class, id);
	}

	public List<Person> getAll() {
		return em.createQuery("SELECT p FROM Person p", Person.class).getResultList();
	}

}
