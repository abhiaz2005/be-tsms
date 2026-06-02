package com.tsms.dto;

import java.util.List;

public class UserFeeDto {
	private UserDto user ;
	private List<FeesDto> fees ;
	public UserDto getUser() {
		return user;
	}
	public void setUser(UserDto user) {
		this.user = user;
	}
	public List<FeesDto> getFees() {
		return fees;
	}
	public void setFees(List<FeesDto> fees) {
		this.fees = fees;
	}
	public UserFeeDto(UserDto user, List<FeesDto> fees) {
		super();
		this.user = user;
		this.fees = fees;
	}
	public UserFeeDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
