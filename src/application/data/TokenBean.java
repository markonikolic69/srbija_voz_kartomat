package application.data;

import com.google.gson.internal.LinkedTreeMap;

public class TokenBean {
	
	private String access_token = "";
	private String token_type = "";
	private int expires_in = 0;
	private String refresh_token = "";
	private String username = "";
	private int id = 0;
	
	public TokenBean (LinkedTreeMap gson_container) {
		access_token = gson_container.getOrDefault("access_token", "").toString();
		token_type = gson_container.getOrDefault("token_type", "").toString();
		expires_in = (int) Double.parseDouble(gson_container.getOrDefault("expires_in", 0).toString());
		refresh_token = gson_container.getOrDefault("refresh_token", "").toString();
		username = gson_container.getOrDefault("username", "").toString();
		id = (int) Double.parseDouble(gson_container.getOrDefault("id", 0).toString());
	}
	
	
	
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getToken_type() {
		return token_type;
	}
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}
	public int getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "TokenBean [access_token=" + access_token + ", token_type=" + token_type + ", expires_in=" + expires_in
				+ ", refresh_token=" + refresh_token + ", username=" + username + ", id=" + id + "]";
	}
	
	
	

}
