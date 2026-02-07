package com.tsms.dto;

import com.tsms.enums.Role;

public class LoginReponse {

    private String token ;
    
    private String userName ;
    
    private String email ;
    
    private Role role ;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public LoginReponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LoginReponse(String token, String userName, String email, Role role) {
		super();
		this.token = token;
		this.userName = userName;
		this.email = email;
		this.role = role;
	}
    
    
    
    
    
}
