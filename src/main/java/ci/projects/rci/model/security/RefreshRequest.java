package ci.projects.rci.model.security;

public class RefreshRequest {

    private String refreshToken;

	public RefreshRequest() {
		super();
	}

	public RefreshRequest(String refreshToken) {
		super();
		this.refreshToken = refreshToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
