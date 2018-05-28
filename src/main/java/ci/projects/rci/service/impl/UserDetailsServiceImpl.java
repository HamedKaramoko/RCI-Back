/**
 * 
 */
package ci.projects.rci.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ci.projects.rci.dao.PersonDAO;
import ci.projects.rci.model.Group;
import ci.projects.rci.model.Person;
import ci.projects.rci.model.security.UserDto;

/**
 * @author hamedkaramoko
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	
	@Autowired
	private PersonDAO personDAO;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		
		Person personFound = personDAO.getByLogin(login);
		
		Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		
		if(personFound == null) {
			LOGGER.debug("Default Person used.");
	        return new UserDto(1L, "admin", passwordEncoder.encode("admin"), "a@b.c",
					authorities, true, LocalDate.now());
	        
		}
		
		LOGGER.debug("Person found: {}", personFound.toString());
		
		if(personFound.getRoles() != null && !personFound.getRoles().isEmpty()) {
			authorities.clear();
			for(Group group: personFound.getRoles()) {
				authorities.add(new SimpleGrantedAuthority(group.getName()));
			}
		}
		return new UserDto(1L, personFound.getLogin(), personFound.getPassword(), personFound.getEmail(),
				authorities, true, LocalDate.now());
	}

}
