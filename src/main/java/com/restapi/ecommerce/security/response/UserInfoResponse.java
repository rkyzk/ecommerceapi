package com.restapi.ecommerce.security.response;

import java.util.List;

public class UserInfoResponse {
	private Long id;
	private String username;
	private String jwtToken;
	private List<String> roles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public UserInfoResponse(Long id, String username, List<String> roles, String jwtToken) {
		this.id = id;
		this.username = username;
		this.roles = roles;
		this.jwtToken = jwtToken;
	};
}

