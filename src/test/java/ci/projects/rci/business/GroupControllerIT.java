/**
 * 
 */
package ci.projects.rci.business;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsNull.*;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;

import ci.projects.rci.config.RootConfig;
import ci.projects.rci.config.WebConfig;
import ci.projects.rci.dao.GroupDAO;
import ci.projects.rci.model.Group;

/**
 * @author hamedkaramoko
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("TEST")
@ContextConfiguration(loader=AnnotationConfigWebContextLoader.class , classes= {WebConfig.class, RootConfig.class})
@WebAppConfiguration
@Transactional
@WithMockUser(username="admin",roles={"ADMIN"})
public class GroupControllerIT {

	private MockMvc mockMvc;
	
	@Autowired
	private GroupDAO GroupDAO;
	
	@Autowired
	private WebApplicationContext wac;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link ci.projects.rci.business.GroupController#saveGroup(ci.projects.rci.model.Group)}.
	 * @throws Exception 
	 */
	@Test
	public void saveGroup() throws Exception {
		Group g = new Group("USER");
		mockMvc.perform(MockMvcRequestBuilders.post("/group")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(this.groupToJSONString(g))
				.accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.content().string(is(notNullValue())));
	}
	
	/**
	 * Test method for {@link ci.projects.rci.business.GroupController#saveGroup(ci.projects.rci.model.Group)}.
	 * @throws Exception 
	 */
	@Test
	@WithAnonymousUser
	public void saveGroupNotBeingAdmin() throws Exception {
		Group g = new Group("USER");
		
		assertThatThrownBy(() -> mockMvc.perform(MockMvcRequestBuilders.post("/group")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(this.groupToJSONString(g))
				.accept(MediaType.APPLICATION_JSON_VALUE))).hasCauseExactlyInstanceOf(AccessDeniedException.class);
	}

	/**
	 * Test method for {@link ci.projects.rci.business.GroupController#deleteGroup(long)}.
	 * @throws Exception 
	 */
	@Test
	public void testDeleteGroup() throws Exception {
		Group g = new Group("USER");
		Long id = this.GroupDAO.save(g);
		Group saved = this.GroupDAO.get(id);
		
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/group/{id}", saved.getId()))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	/**
	 * Test method for {@link ci.projects.rci.business.GroupController#getGroup(long)}.
	 * @throws Exception 
	 */
	@Test
	public void GroupNotFound() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.get("/group/{id}", 1)
				.accept(MediaType.APPLICATION_JSON_VALUE))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isNotFound());
		
		mockMvc.perform(
				MockMvcRequestBuilders.get("/group/name/{name}", "nothing")
				.accept(MediaType.APPLICATION_JSON_VALUE))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	/**
	 * Test method for {@link ci.projects.rci.business.GroupController#getGroup(long)}.
	 * @throws Exception 
	 */
	@Test
	public void GroupFoundByName() throws Exception {
		Group g = new Group("USER");
		Long id = this.GroupDAO.save(g);
		Group saved = this.GroupDAO.get(id);
		mockMvc.perform(
				MockMvcRequestBuilders.get("/group/name/{name}", "USER")
				.accept(MediaType.APPLICATION_JSON_VALUE))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(saved.getName())));
	}
	
	/**
	 * Test method for {@link ci.projects.rci.business.GroupController#getGroup(long)}.
	 * @throws Exception 
	 */
	@Test
	public void GroupFound() throws Exception {
		Group g = new Group("USER");
		Long id = this.GroupDAO.save(g);
		Group saved = this.GroupDAO.get(id);
		mockMvc.perform(
				MockMvcRequestBuilders.get("/group/{id}", saved.getId())
				.accept(MediaType.APPLICATION_JSON_VALUE))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(saved.getName())));
	}

	/**
	 * Test method for {@link ci.projects.rci.business.GroupController#getAllGroup()}.
	 * @throws Exception 
	 */
	@Test
	public void testGetAllGroup() throws Exception {
		final Group g1 = new Group("USER");
		final Group g2 = new Group("ARCHITECT");
		this.GroupDAO.save(g1);
		this.GroupDAO.save(g2);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/group/list")
				.accept(MediaType.APPLICATION_JSON_VALUE))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.hasSize(2)));
	}
	
	private String groupToJSONString(Group s){
		Gson gson = new Gson();
		return gson.toJson(s);
	}

}
