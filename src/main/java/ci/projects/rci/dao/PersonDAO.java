/**
 * 
 */
package ci.projects.rci.dao;

import java.util.List;

import ci.projects.rci.model.Person;

/**
 * @author hamedkaramoko
 *
 */
public interface PersonDAO {
	
	Person save(Person personToSave);
	Person update(Person personToSave);
	Person delete(long idPersonToDelete);
	Person get(long id);
	Person getByLogin(String login);
	List<Person> getAll();

}
