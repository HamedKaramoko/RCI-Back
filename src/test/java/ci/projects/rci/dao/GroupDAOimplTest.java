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
import ci.projects.rci.model.Group;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("TEST")
@ContextConfiguration(loader=AnnotationConfigContextLoader.class , classes= {RootConfig.class})
@Transactional
@Rollback(true)
public class GroupDAOimplTest {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private GroupDAO groupDAO;

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	private void insertGroup(){
		Group g1 = new Group("ARCHITECT");
		saveGroup(g1);
	}
	
	private Long saveGroup(Group g) {
		em.persist(g);
		return g.getId();
	}
	
	/****************************SAVE***************************/

	/**
	 * Save new group
	 */
	@Test
	public void nominalSave() {
		Group s = new Group("ARCHITECT");
		Long id = groupDAO.save(s);
		assertThat(id, is(notNullValue()));
	}

	/**
	 * Attempt to save a group with label already saved
	 */
	@Test(expected=PersistenceException.class)
	public void violationLabelConstraintSave() {
		
		insertGroup();
		Group s = new Group("ARCHITECT");
		groupDAO.save(s);
		em.flush();
	}
	
	/****************************DELETE***************************/

	/**
	 * Delete existing group
	 */
	@Test
	public void nominalDelete() {
		Group g = new Group("ARCHITECT");
		Long id = saveGroup(g);
		
		groupDAO.delete(id);
		
		Group groupDeleted = em.find(Group.class, id);
		
		assertThat(groupDeleted, is(nullValue()));
	}
	
	/**
	 * Delete non existing group
	 */
	@Test(expected=InvalidDataAccessApiUsageException.class)
	public void deleteNotExistingGroup() {
		groupDAO.delete(0L);
		fail("You cannot delete a non-existing group");
	}


	/****************************GET***************************/

	/**
	 * Save a group and then try to find it
	 */
	@Test
	public void getExistingGroup() {
		Group s1 = new Group("ARCHITECT");
		Long id = saveGroup(s1);
		
		Group groupFound = groupDAO.get(id);
		
		Group groupFoundByName = groupDAO.getByName("ARCHITECT");
		
		assertThat(groupFound.getId(), is(id));
		assertThat(groupFound.getName(), is("ARCHITECT"));
		
		assertThat(groupFound.getId(), is(groupFoundByName.getId()));
	}
	
	
	/**
	 * Attempt to get a non existing group
	 */
	@Test
	public void getNotExistingGroup() {
		Group groupFound = groupDAO.get(0L);
		assertThat(groupFound, is(nullValue()));
	}
	
	/**
	 * Attempt to get a non existing group by a name
	 */
	@Test(expected=EmptyResultDataAccessException.class)
	public void getNotExistingGroupByName() {
		groupDAO.getByName("nothing");
		fail("There is no group with 'nothing' as name.");
	}
	
	/****************************GET ALL***************************/

	/**
	 * Attempt to find groups in an group table containing group
	 */
	@Test
	public void getAllGroupWithSavedGroups() {
		Group g1 = new Group("ARCHITECT1");
		Group g2 = new Group("ARCHITECT2");
		Group g3 = new Group("ARCHITECT3");
		
		saveGroup(g1);
		saveGroup(g2);
		saveGroup(g3);
		
		List<Group> groups = groupDAO.getAll();
		assertThat(groups.size(), is(3));
	}
	
	/**
	 * Attempt to find groups in an empty-data group table
	 */
	@Test
	public void getAllGroupWithNoGroups() {
		
		List<Group> groups = groupDAO.getAll();
		assertThat(groups.size(), is(0));
	}

}
