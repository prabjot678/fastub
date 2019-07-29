package com.fastub.api.dto;

import java.util.ArrayList;
import java.util.List;

import com.fastub.api.model.ResponseHeader;

public class ApiDto {
	
	private String appName;
	private String url;
	private String description;
	private String httpMethod;
	
	private List<ApiResponseDto> responseDtos;
	
	private List<ResponseHeader> responseHeaders = new ArrayList<ResponseHeader>();
	
	
	
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<ApiResponseDto> getResponseDtos() {
		return responseDtos;
	}
	public void setResponseDtos(List<ApiResponseDto> responseDtos) {
		this.responseDtos = responseDtos;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getHttpMethod() {
		return httpMethod;
	}
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}
	public List<ResponseHeader> getResponseHeaders() {
		return responseHeaders;
	}
	public void setResponseHeaders(List<ResponseHeader> responseHeaders) {
		this.responseHeaders = responseHeaders;
	}
	
}
