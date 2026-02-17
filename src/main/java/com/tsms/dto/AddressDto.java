package com.tsms.dto;

import com.tsms.entity.Address;

public class AddressDto {
	private Long id;

    private String street;

    private String city;

    private String state;

    private String pincode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public AddressDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AddressDto(Long id, String street, String city, String state, String pincode) {
		super();
		this.id = id;
		this.street = street;
		this.city = city;
		this.state = state;
		this.pincode = pincode;
	}

	public Address convertToEntity() {
		return new Address(
				this.street!=null?this.street:null,
				this.city!=null?this.city:null,
				this.state!=null?this.state:null,
				this.pincode!=null?this.pincode:null);
	}
    
    
}
