package com.restapi.ecommerce.payload;

import java.util.List;

public class LoginResponse {
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	private String username;
	private String jwtToken;
	private List<String> roles;

	public LoginResponse(String username, List<String> roles, String jwtToken) {
		this.username = username;
		this.roles = roles;
		this.jwtToken = jwtToken;
	};
}

