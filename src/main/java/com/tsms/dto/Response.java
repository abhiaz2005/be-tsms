package com.tsms.dto;


import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@Component
@JsonInclude(Include.NON_NULL)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Response<T> {

	private int responseCode;
	private String responseDescription;
	private T data;
	private T requirementDetails;


	public T getData() {
		return data;
	}

	public T getRequirementDetails() {
		return requirementDetails;
	}

	public void setRequirementDetails(T requirementDetails) {
		this.requirementDetails = requirementDetails;
	}

	public void setData(T data) {
		this.data = data;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseDescription() {
		return responseDescription;
	}

	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}



	public Response(int responseCode, String responseDescription, T data) {
		super();
		this.responseCode = responseCode;
		this.responseDescription = responseDescription;
		this.data = data;
	}

	public Response() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}