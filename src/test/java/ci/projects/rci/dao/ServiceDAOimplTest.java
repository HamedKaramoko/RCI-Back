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
	
	private void insertService(){
		Service s1 = new Service("Others", 10f);
		serviceDAO.save(s1);
	}
	
	/****************************SAVE***************************/

	/**
	 * Save new service
	 */
	@Test
	public void nominalSave() {
		Service s = new Service("ONI", 200f);
		Service serviceSaved = serviceDAO.save(s);
		assertThat(serviceSaved.getLabel(), is(s.getLabel()));
		assertThat(serviceSaved.getId(), is(notNullValue()));
	}

	/**
	 * Attempt to save a service with label already saved
	 */
	@Test(expected=PersistenceException.class)
	public void violationLabelConstraintSave() {
		
		insertService();
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
		Service serviceSaved = serviceDAO.save(s);
		
		serviceSaved.setLabel("Passport");
		
		Service serviceUpdated = serviceDAO.update(serviceSaved);
		
		assertThat(serviceUpdated.getId(), is(serviceSaved.getId()));
		assertThat(serviceUpdated.getLabel(), is("Passport"));
	}
	
	/**
	 * Update non existing service.
	 * At first a service with a null id and then with an unknown id
	 */
	@Test
	public void updateNotExistingService() {
		
		Service service = new Service();
		service.setId(null);
		
		Service serviceUpdated = serviceDAO.update(service);
		
		assertThat(serviceUpdated, is(nullValue()));
		
		service.setId(0L);
		serviceUpdated = serviceDAO.update(service);
		
		assertThat(serviceUpdated, is(nullValue()));
	}
	
	/****************************DELETE***************************/

	/**
	 * Delete existing service
	 */
	@Test
	public void nominalDelete() {
		Service s = new Service("ONI", 200f);
		Service serviceSaved = serviceDAO.save(s);
		
		Service serviceDeleted = serviceDAO.delete(serviceSaved.getId());
		
		assertThat(serviceDeleted.getId(), is(serviceSaved.getId()));
	}
	
	/**
	 * Delete non existing service
	 */
	@Test
	public void deleteNotExistingService() {
		
		Service serviceDeleted = serviceDAO.delete(0);
		
		assertThat(serviceDeleted, is(nullValue()));
	}


	/****************************GET***************************/

	/**
	 * Save a service and then try to find it
	 */
	@Test
	public void getExistingService() {
		Service s1 = new Service("ONI", 200f);
		Service serviceSaved = serviceDAO.save(s1);
		Service serviceFound = serviceDAO.get(serviceSaved.getId());
		
		assertThat(serviceFound.getId(), is(serviceSaved.getId()));
		assertThat(serviceFound.getLabel(), is(serviceSaved.getLabel()));
	}
	
	
	/**
	 * Attempt to get a non existing service
	 */
	@Test
	public void getNotExistingService() {
		Service serviceFound = serviceDAO.get(0);
		assertThat(serviceFound, is(nullValue()));
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
		
		serviceDAO.save(s1);
		serviceDAO.save(s2);
		serviceDAO.save(s3);
		
		List<Service> services = serviceDAO.getAll();
		System.out.println(services);
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
