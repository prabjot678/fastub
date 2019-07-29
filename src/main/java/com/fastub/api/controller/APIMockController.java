package com.fastub.api.controller;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fastub.api.ResponseUtil;
import com.fastub.api.exception.EntityNotFoundException;
import com.fastub.api.model.ApiRequest;
import com.fastub.api.service.APIMockService;

@RestController
public class APIMockController {

	@Autowired
	private HttpServletRequest httpServletRequest;

	@Autowired
	private APIMockService apiMockService;

	@GetMapping(value = "/**", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getMockResponse() throws JsonParseException, JsonMappingException, IOException {

		Optional<ApiRequest> apiRequest = apiMockService.getMockResponse(httpServletRequest.getRequestURI(), "GET");

		if (!apiRequest.isPresent()) {

			throw new EntityNotFoundException("End point not found.");
		}
		
		return ResponseUtil.generateResponse(apiRequest.get());
	}
	
	@PostMapping(value = "/**", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> postMockResponse() throws JsonParseException, JsonMappingException, IOException {

		Optional<ApiRequest> apiRequest = apiMockService.getMockResponse(httpServletRequest.getRequestURI(), "POST");

		if (!apiRequest.isPresent()) {

			throw new EntityNotFoundException("End point not found.");
		}

		return ResponseUtil.generateResponse(apiRequest.get());

	}
	
	@DeleteMapping(value = "/**", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> deleteMockResponse() throws JsonParseException, JsonMappingException, IOException {

		Optional<ApiRequest> apiRequest = apiMockService.getMockResponse(httpServletRequest.getRequestURI(), "DELETE");

		if (!apiRequest.isPresent()) {

			throw new EntityNotFoundException("End point not found.");
		}

		return ResponseUtil.generateResponse(apiRequest.get());

	}
	
	@PutMapping(value = "/**", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> putMockResponse() throws JsonParseException, JsonMappingException, IOException {

		Optional<ApiRequest> apiRequest = apiMockService.getMockResponse(httpServletRequest.getRequestURI(), "PUT");

		if (!apiRequest.isPresent()) {

			throw new EntityNotFoundException("End point not found.");
		}

		return ResponseUtil.generateResponse(apiRequest.get());

	}
	
	@PatchMapping(value = "/**", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> patchMockResponse() throws JsonParseException, JsonMappingException, IOException {

		Optional<ApiRequest> apiRequest = apiMockService.getMockResponse(httpServletRequest.getRequestURI(), "PATCH");

		if (!apiRequest.isPresent()) {

			throw new EntityNotFoundException("End point not found.");
		}

		return ResponseUtil.generateResponse(apiRequest.get());

	}

}
