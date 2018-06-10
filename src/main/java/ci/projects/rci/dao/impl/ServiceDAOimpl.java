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

	@Override
	public Long save(Service serviceToSave) {
		em.persist(serviceToSave);
		return serviceToSave.getId();
	}

	@Override
	public void update(Service serviceToUpdate) {
		if(serviceToUpdate == null){
			throw new IllegalArgumentException("The updated service cannot be null.");
		}
		em.createQuery("SELECT s FROM Service s WHERE s.id = :id", Service.class).setParameter("id", serviceToUpdate.getId()).getSingleResult();
		em.merge(serviceToUpdate);
	}

	@Override
	public void delete(long idServiceToDelete) {
		Service serviceToDelete = get(idServiceToDelete);
		em.remove(serviceToDelete);
	}

	@Override
	public Service get(long id) {
		return em.find(Service.class, id);
	}
	
	@Override
	public Service getByLabel(String label) {
		return em.createQuery("SELECT s FROM Service s WHERE s.label = :label", Service.class).setParameter("label", label).getSingleResult();
	}

	@Override
	public List<Service> getAll() {
		return em.createQuery("SELECT s FROM Service s", Service.class).getResultList();
	}

}
