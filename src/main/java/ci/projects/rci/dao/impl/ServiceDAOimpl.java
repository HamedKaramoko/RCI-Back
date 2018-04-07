/**
 * 
 */
package ci.projects.rci.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import ci.projects.rci.dao.ServiceDAO;
import ci.projects.rci.model.Service;

/**
 * @author hamedkaramoko
 *
 */
@Repository
public class ServiceDAOimpl implements ServiceDAO {
	
	@PersistenceContext
	private EntityManager em;

	public Service save(Service serviceToSave) {
		em.persist(serviceToSave);
		return serviceToSave;
	}

	public Service update(Service serviceToSave) {
		return em.merge(serviceToSave);
	}

	public Service delete(long idServiceToDelete) {
		Service serviceToDelete = get(idServiceToDelete);
		em.remove(serviceToDelete);
		return serviceToDelete;
	}

	public Service get(long id) {
		return em.find(Service.class, id);
	}

	public List<Service> getAll() {
		return em.createQuery("SELECT s FROM Service s", Service.class).getResultList();
	}

}
