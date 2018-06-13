/**
 * 
 */
package ci.projects.rci.business;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;

import ci.projects.rci.config.RootConfig;
import ci.projects.rci.config.UserAuthentication;
import ci.projects.rci.config.WebConfig;
import ci.projects.rci.model.security.AuthenticationParam;
import ci.projects.rci.model.security.JwtTokens;
import ci.projects.rci.service.AuthenticationService;
import ci.projects.rci.service.JwtTokenService;

/**
 * @author hamedkaramoko
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigWebContextLoader.class , classes= { WebConfig.class, RootConfig.class})
@ActiveProfiles("TEST")
@WebAppConfiguration
public class AuthenticationControllerIT {

	@InjectMocks
	private AuthenticationController authenticationController;
	
	@Mock
	@Autowired
	private JwtTokenService jwtTokenService;
	@Mock
	@Autowired
	private AuthenticationService authenticationService;

	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext wac;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		authenticationController.setAuthenticationService(authenticationService);
		authenticationController.setJwtTokenService(jwtTokenService);
		mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
		//this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	private String authenticationParamToJSONString(AuthenticationParam aP){
		Gson gson = new Gson();
		return gson.toJson(aP);
	}

	/**
	 * Test method for {@link ci.projects.rci.business.AuthenticationController#signin(ci.projects.rci.model.security.AuthenticationParam)}.
	 * @throws Exception 
	 */
	@Test
	public void testSigninAuthenticationParam() throws Exception {

		/*AuthenticationParam aP = new AuthenticationParam("hamed", "hamed");

		Authentication authentication = new UserAuthenticationWithIsAuthenticatedTrue();
		
		Mockito.when(authenticationService.authenticate(aP)).thenReturn(authentication);
		
		Mockito.when(jwtTokenService.createTokens(authentication)).thenReturn(new JwtTokens("NewToken", "newRefreshToken"));

		mockMvc.perform(MockMvcRequestBuilders.post("/authentication/signin")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(this.authenticationParamToJSONString(aP))
				.accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.jsonPath("$.token", Matchers.is("NewToken")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken", Matchers.is("newRefreshToken")));*/


		//Mockito.verify(authenticationService).authenticate(aP);

		//		List<GrantedAuthority> grantedAuthority = Arrays.asList(new SimpleGrantedAuthority("USER"), new SimpleGrantedAuthority("ADMIN"));
		//		
		//		UserDto userDto = new UserDto(1L, "hamed", "hamed", "a@b.c", grantedAuthority, true, LocalDate.now());
	}

	/**
	 * Test method for {@link ci.projects.rci.business.AuthenticationController#signin(ci.projects.rci.model.security.AuthenticationParam)}.
	 * @throws Exception 
	 */
	@Test
	public void testSigninAuthenticationParamWithNotRecognizeUser() throws Exception {

		/*AuthenticationParam aP = new AuthenticationParam("hamed", "hamed");

		Mockito.when(authenticationService.authenticate(aP)).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.post("/authentication/signin")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(this.authenticationParamToJSONString(aP))
				.accept(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(MockMvcResultMatchers.status().isUnauthorized());*/

		//Mockito.verify(authenticationService).authenticate(aP);
	}

	/**
	 * Test method for {@link ci.projects.rci.business.AuthenticationController#signin(ci.projects.rci.model.security.RefreshRequest)}.
	 */
	@Test
	public void testSigninRefreshRequest() {
		//fail("Not yet implemented");
	}

	private class UserAuthenticationWithIsAuthenticatedTrue extends UserAuthentication{
		private static final long serialVersionUID = -1680438340959950694L;

		@Override
		public boolean isAuthenticated() {
			return true;
		}
	}

}
