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
	public Service save(Service serviceToSave) {
		em.persist(serviceToSave);
		return serviceToSave;
	}

	@Override
	public Service update(Service serviceToUpdate) {
		Service service = null;
		if(serviceToUpdate.getId() == null){
			return null;
		}
		service = get(serviceToUpdate.getId());
		if(service == null){
			return null;
		}
		return em.merge(serviceToUpdate);
	}

	@Override
	public Service delete(long idServiceToDelete) {
		Service serviceToDelete = get(idServiceToDelete);
		if(serviceToDelete != null){
			em.remove(serviceToDelete);
		}
		return serviceToDelete;
	}

	@Override
	public Service get(long id) {
		return em.find(Service.class, id);
	}
	
	@Override
	public Service getByName(String name) {
		return em.createQuery("SELECT s FROM Person s WHERE p.name = :name", Service.class).setParameter("name", name).getSingleResult();
	}

	@Override
	public List<Service> getAll() {
		return em.createQuery("SELECT s FROM Service s", Service.class).getResultList();
	}

}
