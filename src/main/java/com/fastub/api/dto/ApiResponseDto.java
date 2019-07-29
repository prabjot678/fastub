package com.fastub.api.dto;

import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ApiResponseDto {

	private List<Map<String,Object>> response;
	private Map<String, Object> response1;
	
	
	private int statusCode;
	private boolean active;
	private Integer id;
	
	
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
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public List<Map<String, Object>> getResponse() {
		return response;
	}
	public void setResponse(List<Map<String, Object>> response) {
		this.response = response;
	}
	public Map<String, Object> getResponse1() {
		return response1;
	}
	public void setResponse1(Map<String, Object> response1) {
		this.response1 = response1;
	}
	
	
	
	
	
	
	
}
