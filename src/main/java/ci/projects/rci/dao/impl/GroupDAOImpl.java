package ci.projects.rci.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import ci.projects.rci.dao.GroupDAO;
import ci.projects.rci.model.Group;

/**
 * @author hamedkaramoko
 *
 */
@Repository
public class GroupDAOImpl implements GroupDAO {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public Long save(Group group) {
		em.persist(group);
		return group.getId();
	}

	@Override
	public void delete(Long id) {
		Group groupToDelete = em.find(Group.class, id);
		em.remove(groupToDelete);
	}

	@Override
	public Group get(Long id) {
		return em.find(Group.class, id);
	}
	
	@Override
	public Group getByName(String name) {
		return em.createQuery("SELECT g FROM Group g WHERE g.name = :name", Group.class).setParameter("name", name).getSingleResult();
	}

	@Override
	public List<Group> getAll() {
		return em.createQuery("SELECT g FROM Group g", Group.class).getResultList();
	}

}
