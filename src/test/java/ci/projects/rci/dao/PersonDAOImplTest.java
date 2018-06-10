package ci.projects.rci.dao;

import static org.junit.Assert.*;

import java.util.List;

import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsNull.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
//import org.junit.runners.Suite.SuiteClasses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import ci.projects.rci.config.RootConfig;
import ci.projects.rci.dao.PersonDAO;
import ci.projects.rci.model.Person;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("TEST")
@ContextConfiguration(loader=AnnotationConfigContextLoader.class , classes= {RootConfig.class})
@Transactional
@Rollback(true)
//@SuiteClasses
public class PersonDAOImplTest {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private PersonDAO personDAO;

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	private void insertPerson(){
		Person p1 = new Person("Hamed", "");
		p1.setEmail("a@a.a");
		savePerson(p1);
	}

	private Long savePerson(Person p) {
		em.persist(p);
		return p.getId();
	}
	
	/****************************SAVE***************************/

	/**
	 * Save new person
	 */
	@Test
	public void nominalSave() {
		Person p = new Person("Mariama", "");
		p.setEmail("b@b.b");
		Long id = personDAO.save(p);
		assertThat(id, is(notNullValue()));
	}

	/**
	 * Attempt to save a person with login already saved
	 */
	@Test(expected=PersistenceException.class)
	public void violationLoginConstraintSave() {
		
		insertPerson();
		Person p = new Person("Hamed", "");
		p.setEmail("c@c.c");
		personDAO.save(p);
		em.flush();
	}

	/**
	 * Attempt to save a person with email already saved
	 */
	@Test(expected=PersistenceException.class)
	public void violationEmailConstraintSave() {
		insertPerson();
		Person p = new Person("Habib", "");
		p.setEmail("a@a.a");
		personDAO.save(p);
		em.flush();
	}
	
	/****************************UPDATE***************************/

	/**
	 * Update existing person
	 */
	@Test
	public void nominalUpdate() {
		Person p = new Person("Mariama", "");
		p.setEmail("b@b.b");
		Long id = savePerson(p);
		Person personSaved = this.em.find(Person.class, id);
		
		personSaved.setEmail("mariama@mariama.ci");
		personDAO.update(personSaved);
		
		Person personUpdated = this.em.find(Person.class, id);
		
		assertThat(personUpdated.getEmail(), is("mariama@mariama.ci"));
	}
	
	/**
	 * Update with null as Person
	 */
	@Test(expected=InvalidDataAccessApiUsageException.class)
	public void updateWithNullPerson() {
		personDAO.update(null);
		fail("You cannot update a null person");
		
	}
	
	/**
	 * Update non existing person.
	 */
	@Test(expected=EmptyResultDataAccessException.class)
	public void updateNotExistingPerson() {
		Person person = new Person();
		person.setId(null);
		
		personDAO.update(person);
		fail("You cannot update a non-existing person");
		
	}
	
	/****************************DELETE***************************/

	/**
	 * Delete existing person
	 */
	@Test
	public void nominalDelete() {
		Person p = new Person("Mariama", "");
		Long id = savePerson(p);
		
		personDAO.delete(id);
		
		Person personDeleted = this.em.find(Person.class, id);
		
		assertThat(personDeleted, is(nullValue()));
	}
	
	/**
	 * Delete non existing person
	 */
	@Test(expected=InvalidDataAccessApiUsageException.class)
	public void deleteNotExistingPerson() {
		personDAO.delete(0L);
		fail("You cannot delete a non-existing person");
	}


	/****************************GET***************************/

	/**
	 * Save a person and then try to find him
	 */
	@Test
	public void getExistingPerson() {
		Person p1 = new Person("test", "test");
		p1.setEmail("t@t.t");
		Long id = savePerson(p1);
		
		Person personFound = personDAO.get(id);
		
		Person personFoundByLogin = personDAO.getByLogin("test");
		
		assertThat(personFound.getId(), is(id));
		assertThat(personFound.getLogin(), is("test"));
		assertThat(personFound.getPassword(), is("test"));
		assertThat(personFound.getEmail(), is("t@t.t"));
		
		// Assert that the result got by id is the same as the one got by login
		assertThat(personFound.getId(), is(personFoundByLogin.getId()));
	}
	
	
	/**
	 * Attempt to get a non existing person
	 */
	@Test
	public void getNotExistingPersonById() {
		Person personFound = personDAO.get(0L);
		assertThat(personFound, is(nullValue()));
	}
	
	/**
	 * Attempt to get a non existing person by a login
	 */
	@Test(expected=EmptyResultDataAccessException.class)
	public void getNotExistingPersonByLogin() {
		personDAO.getByLogin("nothing");
		fail("There is no person with 'nothing' as login.");
	}
	
	/****************************GET ALL***************************/

	/**
	 * Attempt to find persons in an person table containing person
	 */
	@Test
	public void getAllPersonWithSavedPersons() {
		Person p1 = new Person("t1", "t1");
		Person p2 = new Person("t2", "t2");
		Person p3 = new Person("t3", "t3");
		
		savePerson(p1);
		savePerson(p2);
		savePerson(p3);
		
		List<Person> persons = personDAO.getAll();
		assertThat(persons.size(), is(3));
	}
	
	/**
	 * Attempt to find persons in an empty-data person table
	 */
	@Test
	public void getAllPersonWithNoPerson() {
		
		List<Person> persons = personDAO.getAll();
		assertThat(persons.size(), is(0));
	}
}