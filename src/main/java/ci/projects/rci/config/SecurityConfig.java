/**
 * 
 */
package ci.projects.rci.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author hamed.karamoko
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static String[] SWAGGER_PATHS = {"/v2/api-docs", "/swagger-resources", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**"};
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("hamedkaramoko").password("hamed").roles("ADMIN");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers(SWAGGER_PATHS).permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin().and()
			.httpBasic();
	}

}
