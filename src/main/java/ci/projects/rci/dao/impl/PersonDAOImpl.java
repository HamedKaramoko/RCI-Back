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


	public Long save(Person personToSave) {
		em.persist(personToSave);
		return personToSave.getId();
	}

	public void update(Person personToUpdate) {
		if(personToUpdate == null) {
			throw new IllegalArgumentException("The updated person cannot be null.");
		}
		em.createQuery("SELECT p FROM Person p WHERE p.id = :id", Person.class).setParameter("id", personToUpdate.getId()).getSingleResult();
		em.merge(personToUpdate);
	}

	public void delete(Long idPersonToDelete) {
		Person personToDelete = em.find(Person.class, idPersonToDelete);
		em.remove(personToDelete);
	}

	public Person get(Long id) {
		return em.find(Person.class, id);
	}

	public Person getByLogin(String login) {
		return em.createQuery("SELECT p FROM Person p WHERE p.login = :login", Person.class).setParameter("login", login).getSingleResult();
	}

	public List<Person> getAll() {
		return em.createQuery("SELECT p FROM Person p", Person.class).getResultList();
	}

}
