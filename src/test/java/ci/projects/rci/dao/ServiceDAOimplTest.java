package ci.projects.rci.dao;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import ci.projects.rci.model.Service;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("TEST")
@ContextConfiguration(loader=AnnotationConfigContextLoader.class , classes= {RootConfig.class})
@Transactional
@Rollback(true)
public class ServiceDAOimplTest {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private ServiceDAO serviceDAO;

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	private Long saveService(Service s) {
		em.persist(s);
		return s.getId();
	}
	
	/****************************SAVE***************************/

	/**
	 * Save new service
	 */
	@Test
	public void nominalSave() {
		Service s = new Service("ONI", 200f);
		Long id = serviceDAO.save(s);
		assertThat(id, is(notNullValue()));
	}

	/**
	 * Attempt to save a service with label already saved
	 */
	@Test(expected=PersistenceException.class)
	public void violationLabelConstraintSave() {
		
		saveService(new Service("Others", 10f));
		
		Service s = new Service("Others", 10f);
		serviceDAO.save(s);
		em.flush();
	}
	
	/****************************UPDATE***************************/

	/**
	 * Update existing service
	 */
	@Test
	public void nominalUpdate() {
		Service s = new Service("ONI", 200f);
		Long id = saveService(s);
		Service serviceSaved = em.find(Service.class, id);
		
		serviceSaved.setLabel("Passport");
		serviceDAO.update(serviceSaved);
		
		Service serviceUpdated = em.find(Service.class, id);
		
		assertThat(serviceUpdated.getLabel(), is("Passport"));
	}
	
	/**
	 * Update with null as service.
	 */
	@Test(expected=InvalidDataAccessApiUsageException.class)
	public void updateWithNullService() {
		serviceDAO.update(null);
		fail("You cannot update a null service");
	}
	
	/**
	 * Update non existing service.
	 */
	@Test(expected=EmptyResultDataAccessException.class)
	public void updateNotExistingService() {
		Service service = new Service();
		service.setId(null);
		
		serviceDAO.update(service);
		fail("You cannot update a non-existing service");
	}
	
	/****************************DELETE***************************/

	/**
	 * Delete existing service
	 */
	@Test
	public void nominalDelete() {
		Service s = new Service("ONI", 200f);
		Long id = saveService(s);
		
		serviceDAO.delete(id);
		
		Service serviceDeleted = em.find(Service.class, id);
		
		assertThat(serviceDeleted, is(nullValue()));
	}
	
	/**
	 * Delete non existing service
	 */
	@Test(expected=InvalidDataAccessApiUsageException.class)
	public void deleteNotExistingService() {
		serviceDAO.delete(0);
		fail("You cannot delete a non-existing service");
	}


	/****************************GET***************************/

	/**
	 * Save a service and then try to find it
	 */
	@Test
	public void getExistingService() {
		Service s1 = new Service("ONI", 200f);
		Long id = saveService(s1);
		
		Service serviceFound = serviceDAO.get(id);
		
		Service serviceFoundByName = serviceDAO.getByLabel("ONI");
		
		assertThat(serviceFound.getId(), is(id));
		assertThat(serviceFound.getLabel(), is("ONI"));
		
		assertThat(serviceFound.getId(), is(serviceFoundByName.getId()));
	}
	
	
	/**
	 * Attempt to get a non existing service
	 */
	@Test
	public void getNotExistingService() {
		Service serviceFound = serviceDAO.get(0L);
		assertThat(serviceFound, is(nullValue()));
	}
	
	/**
	 * Attempt to get a non existing service by a name
	 */
	@Test(expected=EmptyResultDataAccessException.class)
	public void getNotExistingServiceByName() {
		serviceDAO.getByLabel("nothing");
		fail("There is no service with 'nothing' as name.");
	}
	
	/****************************GET ALL***************************/

	/**
	 * Attempt to find services in an service table containing service
	 */
	@Test
	public void getAllServiceWithSavedServices() {
		Service s1 = new Service("ONI1", 10f);
		Service s2 = new Service("ONI2", 10f);
		Service s3 = new Service("ONI3", 10f);
		
		saveService(s1);
		saveService(s2);
		saveService(s3);
		
		List<Service> services = serviceDAO.getAll();
		assertThat(services.size(), is(3));
	}
	
	/**
	 * Attempt to find services in an empty-data service table
	 */
	@Test
	public void getAllServiceWithNoServices() {
		
		List<Service> services = serviceDAO.getAll();
		assertThat(services.size(), is(0));
	}

}
