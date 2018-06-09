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
	
	Long save(Person personToSave);
	/**
	 * @param personToUpdate
	 * 
	 * @throws IllegalArgumentException when personToUpdate is null.
	 */
	void update(Person personToUpdate);
	void delete(Long idPersonToDelete);
	Person get(Long id);
	Person getByLogin(String login);
	List<Person> getAll();

}
