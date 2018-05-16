package ci.projects.rci.dao;

import static org.junit.Assert.*;

import java.util.List;

import  static org.hamcrest.core.Is.*;
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
		personDAO.save(p1);
	}
	
	/****************************SAVE***************************/

	/**
	 * Save new person
	 */
	@Test
	public void nominalSave() {
		Person p = new Person("Mariama", "");
		p.setEmail("b@b.b");
		Person personSaved = personDAO.save(p);
		assertThat(personSaved.getLogin(), is(p.getLogin()));
		assertThat(personSaved.getId(), is(notNullValue()));
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
		Person personSaved = personDAO.save(p);
		
		personSaved.setEmail("mariama@mariama.ci");
		
		Person personUpdated = personDAO.update(personSaved);
		
		assertThat(personUpdated.getId(), is(personSaved.getId()));
		assertThat(personUpdated.getEmail(), is("mariama@mariama.ci"));
	}
	
	/**
	 * Update non existing person.
	 * At first a person with a null id and then with an unknown id
	 */
	@Test
	public void updateNotExistingPerson() {
		
		Person person = new Person();
		person.setId(null);
		
		Person personUpdated = personDAO.update(person);
		
		assertThat(personUpdated, is(nullValue()));
		
		person.setId(0L);
		personUpdated = personDAO.update(person);
		
		assertThat(personUpdated, is(nullValue()));
	}
	
	/****************************DELETE***************************/

	/**
	 * Delete existing person
	 */
	@Test
	public void nominalDelete() {
		Person p = new Person("Mariama", "");
		Person personSaved = personDAO.save(p);
		
		Person personDeleted = personDAO.delete(personSaved.getId());
		
		assertThat(personDeleted.getId(), is(personSaved.getId()));
	}
	
	/**
	 * Delete non existing person
	 */
	@Test
	public void deleteNotExistingPerson() {
		
		Person personDeleted = personDAO.delete(0);
		
		assertThat(personDeleted, is(nullValue()));
	}


	/****************************GET***************************/

	/**
	 * Save a person and then try to find him
	 */
	@Test
	public void getExistingPerson() {
		Person p1 = new Person("test", "test");
		p1.setEmail("t@t.t");
		Person personSaved = personDAO.save(p1);
		Person personFound = personDAO.get(personSaved.getId());
		
		Person personFoundByLogin = personDAO.getByLogin(personSaved.getLogin());
		
		assertThat(personFound.getId(), is(personSaved.getId()));
		assertThat(personFound.getLogin(), is(personSaved.getLogin()));
		assertThat(personFound.getPassword(), is(personSaved.getPassword()));
		assertThat(personFound.getEmail(), is(personSaved.getEmail()));
		
		// Assert that the result got by id is the same as the one got by login
		assertThat(personFound.getId(), is(personFoundByLogin.getId()));
	}
	
	
	/**
	 * Attempt to get a non existing person
	 */
	@Test
	public void getNotExistingPerson() {
		Person personFound = personDAO.get(0);
		Person personFoundByLogin = personDAO.getByLogin("nothing");
		
		assertThat(personFound, is(nullValue()));
		assertThat(personFoundByLogin, is(nullValue()));
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
		
		personDAO.save(p1);
		personDAO.save(p2);
		personDAO.save(p3);
		
		List<Person> persons = personDAO.getAll();
		System.out.println(persons);
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