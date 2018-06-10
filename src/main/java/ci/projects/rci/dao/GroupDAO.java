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
	
	Long save(Group group);
	void delete(Long id);
	Group get(Long id);
	Group getByName(String name);
	List<Group> getAll();
}
