/**
 * 
 */
package ci.projects.rci.business;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import ci.projects.rci.dao.PersonDAO;
import ci.projects.rci.model.Person;

/**
 * @author hamedkaramoko
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("TEST")
@ContextConfiguration(loader=AnnotationConfigWebContextLoader.class , classes= {WebConfig.class, RootConfig.class})
@WebAppConfiguration
@Transactional
public class PersonControllerIT {

	private MockMvc mockMvc;

	@Autowired
	private PersonDAO personDAO;

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
	 * Test method for {@link ci.projects.rci.business.PersonController#saveUser(ci.projects.rci.model.Person)}.
	 * @throws Exception 
	 */
	@Test
	public void savePerson() throws Exception {
		Person p = new Person("Hamed", "");
		mockMvc.perform(MockMvcRequestBuilders.post("/person")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(this.personToJSONString(p))
				.accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.jsonPath("$.login", Matchers.is(p.getLogin())));
	}

	/**
	 * Test method for {@link ci.projects.rci.business.PersonController#updateUser(ci.projects.rci.model.Person)}.
	 * @throws Exception 
	 */
	@Test
	public void updatePerson() throws Exception {
		final Person p = new Person("Hamed", "");
		Person personSaved = this.personDAO.save(p);
		personSaved.setEmail("a@b.c");
		personSaved.setPassword("aaaa");
		mockMvc.perform(MockMvcRequestBuilders.put("/person")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(this.personToJSONString(personSaved))
				.accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.jsonPath("$.login", Matchers.is(personSaved.getLogin())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.password", Matchers.is(personSaved.getPassword())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(personSaved.getEmail())));
	}

	/**
	 * Test method for {@link ci.projects.rci.business.PersonController#deleteUser(long)}.
	 * @throws Exception 
	 */
	@Test
	public void deletePerson() throws Exception {
		Person p = new Person("Hamed", "");
		Person savedPerson = this.personDAO.save(p);
		mockMvc.perform(
				delete("/person/{id}", savedPerson.getId()))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.content().string(savedPerson.getLogin()));
	}

	/**
	 * Test method for {@link ci.projects.rci.business.PersonController#getUser(long)}.
	 * @throws Exception 
	 */
	@Test
	public void personNotFound() throws Exception {

		mockMvc.perform(
				get("/person/{id}", 1)
				.accept(MediaType.APPLICATION_JSON_VALUE))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	/**
	 * Test method for {@link ci.projects.rci.business.PersonController#getUser(long)}.
	 * @throws Exception 
	 */
	@Test
	public void personFound() throws Exception {
		Person p = new Person("Hamed", "");
		Person savedPerson = this.personDAO.save(p);
		mockMvc.perform(
				get("/person/{id}", savedPerson.getId())
				.accept(MediaType.APPLICATION_JSON_VALUE))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.login", Matchers.is(savedPerson.getLogin())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.password", Is.is(savedPerson.getPassword())));
	}
	
	/**
	 * Test method for {@link ci.projects.rci.business.PersonController#getUser(long)}.
	 * @throws Exception 
	 */
	@Test
	public void personFoundByLogin() throws Exception {
		Person p = new Person("Hamed", "");
		Person savedPerson = this.personDAO.save(p);
		mockMvc.perform(
				get("/person/login/{login}", savedPerson.getLogin())
				.accept(MediaType.APPLICATION_JSON_VALUE))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.login", Matchers.is(savedPerson.getLogin())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.password", Is.is(savedPerson.getPassword())));
	}

	/**
	 * Test method for {@link ci.projects.rci.business.PersonController#getAllUsers()}.
	 * @throws Exception 
	 */
	@Test
	public void twoPersonsFound() throws Exception {
		final Person p1 = new Person("Hamed", "");
		final Person p2 = new Person("Mariama", "");
		this.personDAO.save(p1);
		this.personDAO.save(p2);

		mockMvc.perform(
				get("/person/list")
				.accept(MediaType.APPLICATION_JSON_VALUE))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.hasSize(2)));
	}

	public String personToJSONString(Person p){
		Gson gson = new Gson();
		return gson.toJson(p);
	}

}
