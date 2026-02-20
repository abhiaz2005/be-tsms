package com.tsms.entity;

import java.util.Date;

import com.tsms.dto.UserDto;
import com.tsms.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;
	
	@Column(name = "name")
	private String name ;
	
	@Column(name = "image_url")
	private String image ;
	
	@Column(name = "email")
	private String email ;
	
	@Column(name = "gender")
	private String gender ;
	
	@Column(name = "password")
	private String password ;
	
	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private Role role ;
	
	@Column(name = "dob")
	private Date dob ;
	
	@Column(name = "father_name")
	private String fatherName ;
	
	@Column(name = "mother_name")
	private String motherName ;
	
	@Column(name = "section")
	private String section ;
	
	@Column(name = "phone_number")
	private String phoneNo ;
	
	@Column(name = "studied_from")
	private Date studiedFrom ;
	
	@ManyToOne
    @JoinColumn(name = "present_address_id")
    private Address presentAddress;

    @ManyToOne
    @JoinColumn(name = "permanent_address_id")
    private Address permanentAddress;
    
    @Column(name = "is_active")
    private Boolean isActive ;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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
	
	

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}


	



	public User(Long id, String name, String image, String email, String gender, String password, Role role, Date dob,
			String fatherName, String motherName, String section, String phoneNo, Date studiedFrom,
			Address presentAddress, Address permanentAddress, Boolean isActive) {
		super();
		this.id = id;
		this.name = name;
		this.image = image;
		this.email = email;
		this.gender = gender;
		this.password = password;
		this.role = role;
		this.dob = dob;
		this.fatherName = fatherName;
		this.motherName = motherName;
		this.section = section;
		this.phoneNo = phoneNo;
		this.studiedFrom = studiedFrom;
		this.presentAddress = presentAddress;
		this.permanentAddress = permanentAddress;
		this.isActive = isActive;
	}

	public UserDto convertToDto() {
	    return new UserDto(
	        this.id ,
	        this.image != null ? this.image : null,
	        this.name != null ? this.name : null,
	        this.email != null ? this.email : null,
	        this.role != null ? this.role : null,
	        this.gender!=null ? this.gender :null ,
	        this.dob != null ? this.dob : null,
	        this.fatherName != null ? this.fatherName : null,
	        this.motherName != null ? this.motherName : null,
	        this.section != null ? this.section : null,
	        this.studiedFrom != null ? this.studiedFrom : null,
	        this.presentAddress != null ? this.presentAddress : null,
	        this.permanentAddress != null ? this.permanentAddress : null
	    );
	}


	
	 
	
    
}
