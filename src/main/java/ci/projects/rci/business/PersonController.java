/**
 * 
 */
package ci.projects.rci.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ci.projects.rci.dao.PersonDAO;
import ci.projects.rci.model.Person;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author hamedkaramoko
 *
 */
@Api(value = "PersonController", description = "REST APIs related to Person Entity!!!!")
@RestController
@RequestMapping(value="/person")
@Transactional
@PreAuthorize("isAuthenticated()")
public class PersonController{
	
	private PersonDAO personDAO;

	/**
	 * @param personDAO
	 */
	@Autowired
	public PersonController(PersonDAO personDAO) {
		super();
		this.personDAO = personDAO;
	}

	@ApiOperation(value="Save one person", response=Person.class)
	@RequestMapping(method=RequestMethod.POST, consumes={MediaType.APPLICATION_JSON_VALUE}, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Person> saveUser(@RequestBody Person personToSave) {
		personToSave.setId(null);
		Person savedPerson = personDAO.save(personToSave);
		return new ResponseEntity<Person>(savedPerson, HttpStatus.CREATED);
	}

	@ApiOperation(value="Update one person", response=Person.class)
	@RequestMapping(method=RequestMethod.PUT, consumes={MediaType.APPLICATION_JSON_VALUE}, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Person> updateUser(@RequestBody Person personToUpdate) {
		Person updatedPerson = personDAO.update(personToUpdate);
		return new ResponseEntity<Person>(updatedPerson, HttpStatus.ACCEPTED);
	}

	@ApiOperation(value="Delete one person")
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteUser(@PathVariable("id") long idPersonToDelete) {
		Person deletedPerson = personDAO.delete(idPersonToDelete);
		return new ResponseEntity<String>(deletedPerson.getLogin(), HttpStatus.ACCEPTED);
	}

	@ApiOperation(value="Get one person", response=Person.class)
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Person> getUser(@PathVariable("id") final long id) {
		Person personFound = personDAO.get(id);
		HttpStatus httpStatus = (personFound != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return new ResponseEntity<Person>(personFound, httpStatus);
	}
	
	@ApiOperation(value="Get one person by his login", response=Person.class)
	@RequestMapping(value="/login/{login}", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Person> getUserByLogin(@PathVariable("login") final String login) {
		Person personFound = personDAO.getByLogin(login);
		HttpStatus httpStatus = (personFound != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return new ResponseEntity<Person>(personFound, httpStatus);
	}

	@ApiOperation(value="Get all persons", response=Person.class)
	@RequestMapping(value="/list", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<Person>> getAllUsers() {
		List<Person> persons = personDAO.getAll();
		HttpStatus httpStatus = (persons != null && !persons.isEmpty()) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return new ResponseEntity<List<Person>>(persons, httpStatus);
	}

}
