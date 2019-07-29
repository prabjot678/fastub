package com.fastub.api.dto;

import java.util.ArrayList;
import java.util.List;

import com.fastub.api.model.ResponseHeader;

public class ResponseHeaderDto {
	
	private List<ResponseHeader> responseHeaders = new ArrayList<ResponseHeader>();

	public List<ResponseHeader> getResponseHeaders() {
		return responseHeaders;
	}

	public void setResponseHeaders(List<ResponseHeader> responseHeaders) {
		this.responseHeaders = responseHeaders;
	}
	
	

}
