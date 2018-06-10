/**
 * 
 */
package ci.projects.rci.dao;

import java.util.List;

import ci.projects.rci.model.Service;

/**
 * @author hamedkaramoko
 *
 */
public interface ServiceDAO {
	
	Long save(Service serviceToSave);
	/**
	 * @param personToUpdate
	 * 
	 * @throws IllegalArgumentException when personToUpdate is null.
	 */
	void update(Service serviceToSave);
	void delete(long idServiceToDelete);
	Service get(long id);
	Service getByLabel(String label);
	List<Service> getAll();

}
