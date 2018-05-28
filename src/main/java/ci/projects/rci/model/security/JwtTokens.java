package ci.projects.rci.model.security;

/**
 * @author hamedkaramoko
 *
 */
public class JwtTokens {

	public String token;
	public String refreshToken;

	public JwtTokens() {
	}

	public JwtTokens(String token, String refreshToken) {
		this.token = token;
		this.refreshToken = refreshToken;
	}

}
