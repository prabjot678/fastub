package com.fastub.api.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ResponseHeader extends Base {
	
	private String key;
	
	@Lob
	private String value;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "api_request_id", nullable = false)
	@JsonIgnore
	private ApiRequest apiRequest;
	
	
	
	
	
	public ApiRequest getApiRequest() {
		return apiRequest;
	}
	public void setApiRequest(ApiRequest apiRequest) {
		this.apiRequest = apiRequest;
	}
	public ResponseHeader() {
		super();
	}
	public ResponseHeader(String key, String value) {
		this.key = key;
		this.value = value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	

}
