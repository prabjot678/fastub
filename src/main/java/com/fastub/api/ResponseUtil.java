package com.fastub.api;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fastub.api.model.ApiRequest;
import com.fastub.api.model.ApiResponse;
import com.fastub.api.model.ResponseHeader;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ResponseUtil {
	
	private static final Logger logger = LogManager.getLogger(ResponseUtil.class);

	private ResponseUtil() {
	}

	public static ResponseEntity<Object> generateResponse(ApiRequest apiRequest) {
		
		logger.info("Method execution starts generateResponse() for API request " + apiRequest);
		
		Optional<ApiResponse>  apiResponse = apiRequest.getApiResponse().stream().filter(apiResponseObj -> apiResponseObj.isActive()).findAny();
		
		
		HttpHeaders servletResponseHeaders = getResponseHeaders(apiRequest);
		
		if(apiResponse != null && !apiResponse.isPresent()) {
			logger.info("API response body is empty " + apiResponse);
			return new ResponseEntity<Object>(null, servletResponseHeaders, HttpStatus.OK);
		}

		JsonElement jsonResponse;
		try {
			jsonResponse = new JsonParser().parse(apiResponse.get().getResponse()).getAsJsonObject();
		} catch (Exception exception) {
			jsonResponse = new JsonParser().parse(apiResponse.get().getResponse()).getAsJsonArray();
		}
		 


		HttpStatus httpStatus = HttpStatus.valueOf(apiResponse.get().getStatusCode());
		
		logger.info("Method execution finished generateResponse() for API request " + jsonResponse.toString());

		return new ResponseEntity<Object>(apiResponse.get().getResponse(), servletResponseHeaders, httpStatus);

	}

	private static HttpHeaders getResponseHeaders(ApiRequest apiRequest) {

		List<ResponseHeader> responseHeaders = apiRequest.getResponseHeaders();

		HttpHeaders servletResponseHeaders = new HttpHeaders();

		responseHeaders.forEach(
				responseHeader -> servletResponseHeaders.set(responseHeader.getKey(), responseHeader.getValue()));

		return servletResponseHeaders;
	}

}
