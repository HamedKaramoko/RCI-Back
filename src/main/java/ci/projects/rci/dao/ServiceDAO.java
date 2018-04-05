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
	
	Service save(Service serviceToSave);
	Service update(Service serviceToSave);
	Service delete(long idServiceToDelete);
	Service get(long id);
	List<Service> getAll();

}
