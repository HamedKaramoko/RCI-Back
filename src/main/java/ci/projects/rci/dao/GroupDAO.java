/**
 * 
 */
package ci.projects.rci.dao;

import java.util.List;

import ci.projects.rci.model.Group;

/**
 * @author hamedkaramoko
 *
 */
public interface GroupDAO {
	
	Group save(Group group);
	Group delete(String name);
	Group get(Long id);
	Group getByName(String name);
	List<Group> getAll();
}
