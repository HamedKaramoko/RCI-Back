/**
 * 
 */
package ci.projects.rci.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ci.projects.rci.model.Person;
import ci.projects.rci.model.security.AuthenticationParam;
import ci.projects.rci.model.security.JwtTokens;
import ci.projects.rci.model.security.RefreshRequest;
import ci.projects.rci.service.AuthenticationService;
import ci.projects.rci.service.JwtTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author hamedkaramoko
 *
 */
@Api(value = "AuthenticationController", description = "REST APIs related for authentication management!!!!")
@RestController
@RequestMapping(value="/authentication")
public class AuthenticationController {
	
	@Autowired
	private JwtTokenService jwtTokenService;
	
	@Autowired
	private AuthenticationService authenticationService;

	/*@Autowired
	public void setJwtTokenService(JwtTokenService jwtTokenService) {
		this.jwtTokenService = jwtTokenService;
	}

	@Autowired
	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}*/

	@ApiOperation(value="Sign In", response=Person.class)
	@RequestMapping(value="/signin", method=RequestMethod.POST, consumes={MediaType.APPLICATION_JSON_VALUE}, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> signin(@RequestBody AuthenticationParam authenticationParam) {
		
        Authentication authentication = this.authenticationService.authenticate(authenticationParam);

        if(authentication != null && authentication.isAuthenticated()) {
            JwtTokens tokens = jwtTokenService.createTokens(authentication);
            return ResponseEntity.ok().body(tokens);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(HttpStatus.UNAUTHORIZED.getReasonPhrase());
	}
	
	@ApiOperation(value="Refresh Token")
	@RequestMapping(value="/refresh", method=RequestMethod.POST, consumes={MediaType.APPLICATION_JSON_VALUE}, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> signin(@RequestBody RefreshRequest refreshRequest) {
		try {
            JwtTokens tokens = jwtTokenService.refreshJwtToken(refreshRequest.getRefreshToken());
            return ResponseEntity.ok().body(tokens);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }
	}

}
