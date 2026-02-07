package com.tsms.entity;

import java.util.Date;

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
	
	@Column(name = "email")
	private String email ;
	
	@Column(name = "password")
	private String password ;
	
	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private Role role ;
	
	@Column(name = "dob")
	private Date dob ;
	
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

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(Long id, String name, String email, String password, Role role, Date dob, Address presentAddress,
			Address permanentAddress, Boolean isActive) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.dob = dob;
		this.presentAddress = presentAddress;
		this.permanentAddress = permanentAddress;
		this.isActive = isActive;
	}

	
	 
	
    
}
