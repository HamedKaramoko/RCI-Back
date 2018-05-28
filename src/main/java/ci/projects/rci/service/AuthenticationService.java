package ci.projects.rci.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.security.core.Authentication;

import ci.projects.rci.model.security.AuthenticationParam;

public interface AuthenticationService {

	Authentication getAuthentication(Jws<Claims> request);
	Authentication authenticate(AuthenticationParam authenticationRequest);

}
