/**
 * 
 */
package ci.projects.rci.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ci.projects.rci.dao.PersonDAO;
import ci.projects.rci.model.Person;

/**
 * @author hamedkaramoko
 *
 */
@RestController
@RequestMapping(value="/person")
public class PersonController{
	
	@Autowired
	private PersonDAO personDAO;

	@Transactional
	@RequestMapping(method=RequestMethod.POST, consumes={MediaType.APPLICATION_JSON_VALUE}, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Person> saveUser(@RequestBody Person personToSave) {
		personToSave.setId(0);
		Person savedPerson = personDAO.save(personToSave);
		return new ResponseEntity<Person>(savedPerson, HttpStatus.CREATED);
	}

	@Transactional
	@RequestMapping(method=RequestMethod.PUT, consumes={MediaType.APPLICATION_JSON_VALUE}, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Person> updateUser(@RequestBody Person personToUpdate) {
		Person updatedPerson = personDAO.update(personToUpdate);
		return new ResponseEntity<Person>(updatedPerson, HttpStatus.ACCEPTED);
	}

	@Transactional
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> deleteUser(@RequestParam("id") long idPersonToDelete) {
		Person deletedPerson = personDAO.delete(idPersonToDelete);
		return new ResponseEntity<String>(deletedPerson.getLogin(), HttpStatus.ACCEPTED);
	}

	@Transactional
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Person> getUser(@PathVariable("id") long id) {
		Person personFound = personDAO.get(id);
		HttpStatus httpStatus = (personFound != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return new ResponseEntity<Person>(personFound, httpStatus);
	}

	@Transactional
	@RequestMapping(value="/list", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<Person>> getAllUsers() {
		List<Person> persons = personDAO.getAll();
		HttpStatus httpStatus = (persons != null && !persons.isEmpty()) ? HttpStatus.FOUND : HttpStatus.NOT_FOUND;
		return new ResponseEntity<List<Person>>(persons, httpStatus);
	}

}
