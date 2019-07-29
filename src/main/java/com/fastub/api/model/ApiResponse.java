package com.fastub.api.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ApiResponse extends Base {

	@Lob
	private String response;
	private int statusCode;
	private boolean active;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "api_request_id", nullable = false)
	@JsonIgnore
	private ApiRequest apiRequest;
	
	
	
	

	public ApiResponse() {
		super();
	}

	public ApiResponse(String response, int statusCode,boolean active,ApiRequest apiRequest) {
		super();
		this.response = response;
		this.statusCode = statusCode;
		this.active = active;
		this.apiRequest = apiRequest;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public ApiRequest getApiRequest() {
		return apiRequest;
	}

	public void setApiRequest(ApiRequest apiRequest) {
		this.apiRequest = apiRequest;
	}

	@Override
	public String toString() {
		return "ApiResponse [response=" + response + ", statusCode=" + statusCode + ", active=" + active
				+ ", apiRequest=" + apiRequest + ", id=" + id + "]";
	}

	
	
	
	
	

}
