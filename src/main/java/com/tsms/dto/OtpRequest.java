package com.tsms.dto;

import jakarta.validation.constraints.NotBlank;

public class OtpRequest {
	
	@NotBlank(message = "Email is required")
	private String email;

	@NotBlank(message = "Otp is required")
	private String otp;
	
	private String name ;

	public OtpRequest(String email, String otp) {
		super();
		this.email = email;
		this.otp = otp;
	}

	public OtpRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	

}
