package com.tsms.dto;

import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

	@NotBlank(message = "Name is required")
	private String name;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;

	private String password;

	@NotNull(message = "Date of birth is required")
	@Past(message = "DOB must be in past")
	private Date dob;

	private Integer age;

	@NotBlank(message = "Father Name is required")
	private String fatherName;

	@NotBlank(message = "Mother Name is required")
	private String motherName;

	@NotNull(message = "Studied From is required")
	@Past(message = "Studied from must be in past")
	private Date studiedFrom;

	private AddressDto presentAddress;

	private AddressDto permanentAddress;

	

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public Date getStudiedFrom() {
		return studiedFrom;
	}

	public void setStudiedFrom(Date studiedFrom) {
		this.studiedFrom = studiedFrom;
	}

	public AddressDto getPresentAddress() {
		return presentAddress;
	}

	public void setPresentAddress(AddressDto presentAddress) {
		this.presentAddress = presentAddress;
	}

	public AddressDto getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(AddressDto permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	public RegisterRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RegisterRequest(String name, String email, String password, Date dob, Integer age) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.dob = dob;
		this.age = age;
	}

	public RegisterRequest( String name, String email, String password, Date dob,
			Integer age, String fatherName, String motherName, Date studiedFrom, AddressDto presentAddress,
			AddressDto permanentAddress) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.dob = dob;
		this.age = age;
		this.fatherName = fatherName;
		this.motherName = motherName;
		this.studiedFrom = studiedFrom;
		this.presentAddress = presentAddress;
		this.permanentAddress = permanentAddress;
	}

}
