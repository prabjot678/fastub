package com.fastub.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class ApiRequest extends Base{
	
	private String url;
	
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "apiRequest")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private List<ApiResponse> apiResponse = new ArrayList<ApiResponse>();
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "apiRequest")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private List<ResponseHeader> responseHeaders;
	
	
	private String httpMethod;
	
	private String paramPosition;
	
	private int urlLength;

	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}



	public List<ApiResponse> getApiResponse() {
		return apiResponse;
	}

	public void setApiResponse(List<ApiResponse> apiResponse) {
		this.apiResponse = apiResponse;
	}


	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getParamPosition() {
		return paramPosition;
	}

	public void setParamPosition(String paramPosition) {
		this.paramPosition = paramPosition;
	}

	public int getUrlLength() {
		return urlLength;
	}

	public void setUrlLength(int urlLength) {
		this.urlLength = urlLength;
	}

	public List<ResponseHeader> getResponseHeaders() {
		return responseHeaders;
	}

	public void setResponseHeaders(List<ResponseHeader> responseHeaders) {
		this.responseHeaders = responseHeaders;
	}

	@Override
	public String toString() {
		return "ApiRequest [url=" + url + ", id=" + id + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
	
	

	

}
