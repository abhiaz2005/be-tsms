package com.tsms.dto;

import java.util.Date;

import com.tsms.entity.Address;
import com.tsms.enums.Role;

public class UserDto {
private Long id ;

private String image ;
	
	private String name ;
	
	private String email ;
	
	private Role role ;
	
	private Date dob ;
	
	private String fatherName ;
	
	private String motherName ;
	
	private String section ;
	
	private Date studiedFrom ;

	private Address presentAddress;

    private Address permanentAddress;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
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

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public Date getStudiedFrom() {
		return studiedFrom;
	}

	public void setStudiedFrom(Date studiedFrom) {
		this.studiedFrom = studiedFrom;
	}

	public Address getPresentAddress() {
		return presentAddress;
	}

	public void setPresentAddress(Address presentAddress) {
		this.presentAddress = presentAddress;
	}

	public Address getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(Address permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	public UserDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserDto(Long id, String image, String name, String email, Role role, Date dob, String fatherName,
			String motherName, String section, Date studiedFrom, Address presentAddress, Address permanentAddress) {
		super();
		this.id = id;
		this.image = image;
		this.name = name;
		this.email = email;
		this.role = role;
		this.dob = dob;
		this.fatherName = fatherName;
		this.motherName = motherName;
		this.section = section;
		this.studiedFrom = studiedFrom;
		this.presentAddress = presentAddress;
		this.permanentAddress = permanentAddress;
	}
    
    
    
    
    
}
