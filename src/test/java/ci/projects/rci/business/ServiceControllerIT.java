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
import ci.projects.rci.dao.ServiceDAO;
import ci.projects.rci.model.Service;

/**
 * @author hamedkaramoko
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("TEST")
@ContextConfiguration(loader=AnnotationConfigWebContextLoader.class , classes= {WebConfig.class, RootConfig.class})
@WebAppConfiguration
@Transactional
public class ServiceControllerIT {

	private MockMvc mockMvc;
	
	@Autowired
	private ServiceDAO serviceDAO;
	
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
	 * Test method for {@link ci.projects.rci.business.ServiceController#saveService(ci.projects.rci.model.Service)}.
	 * @throws Exception 
	 */
	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	public void testSaveService() throws Exception {
		Service s = new Service("others", 100f);
		mockMvc.perform(MockMvcRequestBuilders.post("/service")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(this.serviceToJSONString(s))
				.accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.content().string(is(notNullValue())));
	}
	
	/**
	 * Test method for {@link ci.projects.rci.business.ServiceController#saveService(ci.projects.rci.model.Service)}.
	 * @throws Exception 
	 */
	@Test
	@WithAnonymousUser
	public void saveServiceNotBeingAdmin() throws Exception {
		Service s = new Service("others", 100f);
		
		assertThatThrownBy(() -> mockMvc.perform(MockMvcRequestBuilders.post("/service")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(this.serviceToJSONString(s))
				.accept(MediaType.APPLICATION_JSON_VALUE))).hasCauseExactlyInstanceOf(AccessDeniedException.class);
	}

	/**
	 * Test method for {@link ci.projects.rci.business.ServiceController#updateService(ci.projects.rci.model.Service)}.
	 * @throws Exception 
	 */
	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	public void testUpdateService() throws Exception {
		final Service s = new Service("others", 100f);
		Long id = this.serviceDAO.save(s);
		Service saved = this.serviceDAO.get(id);
		saved.setDescription("Other service");
		mockMvc.perform(MockMvcRequestBuilders.put("/service")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(this.serviceToJSONString(saved))
				.accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}
	
	/**
	 * Test method for {@link ci.projects.rci.business.ServiceController#updateService(ci.projects.rci.model.Service)}.
	 * @throws Exception 
	 */
	@Test
	@WithAnonymousUser
	public void updateServiceNotBeingAdmin() throws Exception {
		final Service s = new Service("others", 100f);
		Long id = this.serviceDAO.save(s);
		Service saved = this.serviceDAO.get(id);
		saved.setDescription("Other service");
		
		assertThatThrownBy(() -> mockMvc.perform(MockMvcRequestBuilders.put("/service")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(this.serviceToJSONString(saved))
				.accept(MediaType.APPLICATION_JSON_VALUE))).hasCauseExactlyInstanceOf(AccessDeniedException.class);
	}

	/**
	 * Test method for {@link ci.projects.rci.business.ServiceController#deleteService(long)}.
	 * @throws Exception 
	 */
	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	public void testDeleteService() throws Exception {
		Service s = new Service("other", 100f);
		Long id = this.serviceDAO.save(s);
		Service saved = this.serviceDAO.get(id);
		
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/service/{id}", saved.getId()))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}
	
	/**
	 * Test method for {@link ci.projects.rci.business.ServiceController#deleteService(long)}.
	 * @throws Exception 
	 */
	@Test
	@WithAnonymousUser
	public void deleteServiceNotBeingAdmin() throws Exception {
		Service s = new Service("other", 100f);
		Long id = this.serviceDAO.save(s);
		Service saved = this.serviceDAO.get(id);
		
		assertThatThrownBy(() -> mockMvc.perform(
				MockMvcRequestBuilders.delete("/service/{id}", saved.getId()))).hasCauseExactlyInstanceOf(AccessDeniedException.class);
	}

	/**
	 * Test method for {@link ci.projects.rci.business.ServiceController#getService(long)}.
	 * @throws Exception 
	 */
	@Test
	public void serviceNotFound() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.get("/service/{id}", 1)
				.accept(MediaType.APPLICATION_JSON_VALUE))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isNotFound());
		
		mockMvc.perform(
				MockMvcRequestBuilders.get("/service/label/{label}", "nothing")
				.accept(MediaType.APPLICATION_JSON_VALUE))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	/**
	 * Test method for {@link ci.projects.rci.business.ServiceController#getService(long)}.
	 * @throws Exception 
	 */
	@Test
	public void serviceFoundByName() throws Exception {
		Service s = new Service("other", 100f);
		Long id = this.serviceDAO.save(s);
		Service saved = this.serviceDAO.get(id);
		mockMvc.perform(
				MockMvcRequestBuilders.get("/service/label/{label}", "other")
				.accept(MediaType.APPLICATION_JSON_VALUE))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.label", Matchers.is(saved.getLabel())));
	}
	
	/**
	 * Test method for {@link ci.projects.rci.business.ServiceController#getService(long)}.
	 * @throws Exception 
	 */
	@Test
	public void serviceFound() throws Exception {
		Service s = new Service("other", 100f);
		Long id = this.serviceDAO.save(s);
		Service saved = this.serviceDAO.get(id);
		mockMvc.perform(
				MockMvcRequestBuilders.get("/service/{id}", saved.getId())
				.accept(MediaType.APPLICATION_JSON_VALUE))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.label", Matchers.is(saved.getLabel())));
	}

	/**
	 * Test method for {@link ci.projects.rci.business.ServiceController#getAllService()}.
	 * @throws Exception 
	 */
	@Test
	public void testGetAllService() throws Exception {
		final Service s1 = new Service("other", 100f);
		final Service s2 = new Service("ONI", 20f);
		this.serviceDAO.save(s1);
		this.serviceDAO.save(s2);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/service/list")
				.accept(MediaType.APPLICATION_JSON_VALUE))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.hasSize(2)));
	}
	
	public String serviceToJSONString(Service s){
		Gson gson = new Gson();
		return gson.toJson(s);
	}

}
