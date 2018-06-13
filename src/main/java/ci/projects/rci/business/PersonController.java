/**
 * 
 */
package ci.projects.rci.business;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class PersonController{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
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
	@RequestMapping(method=RequestMethod.POST, consumes={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> saveUser(@RequestBody Person personToSave) {
		personToSave.setId(null);
		personToSave.setPassword(this.passwordEncoder.encode(personToSave.getPassword()));
		Long id = personDAO.save(personToSave);
		return new ResponseEntity<>(id, HttpStatus.CREATED);
	}

	@ApiOperation(value="Update one person", response=Person.class)
	@RequestMapping(method=RequestMethod.PUT, consumes={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> updateUser(@RequestBody Person personToUpdate) {
		personDAO.update(personToUpdate);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value="Delete one person")
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteUser(@PathVariable("id") long idPersonToDelete) {
		personDAO.delete(idPersonToDelete);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value="Get one person", response=Person.class)
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Person> getUser(@PathVariable("id") final long id) {
		Person personFound = personDAO.get(id);
		HttpStatus httpStatus = (personFound != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return new ResponseEntity<Person>(personFound, httpStatus);
	}
	
	@ApiOperation(value="Get one person by his login", response=Person.class)
	@RequestMapping(value="/login/{login}", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Person> getUserByLogin(@PathVariable("login") final String login) {
		Person personFound = null;
		HttpStatus httpStatus = HttpStatus.NOT_FOUND;
		try {
			personFound = personDAO.getByLogin(login);
			httpStatus = HttpStatus.OK;
		}catch(EmptyResultDataAccessException erdae) {
			LOGGER.info("Person with login '{}' not found because of {}", login, erdae);
		}
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
