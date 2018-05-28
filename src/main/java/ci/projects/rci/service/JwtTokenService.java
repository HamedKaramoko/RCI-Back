package ci.projects.rci.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.security.core.Authentication;

import ci.projects.rci.model.security.JwtTokens;
import ci.projects.rci.model.security.UserDto;

public interface JwtTokenService {

    JwtTokens createTokens(Authentication authentication);
    String createToken(UserDto user);
    String createRefreshToken(UserDto user);

    JwtTokens refreshJwtToken(String token);
    Jws<Claims> validateJwtToken(String token);

}
