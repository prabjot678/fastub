package com.fastub.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fastub.api.dto.ApiDto;
import com.fastub.api.dto.ApiResponseDto;
import com.fastub.api.dto.HttpStatusCodeDto;
import com.fastub.api.dto.ResponseHeaderDto;
import com.fastub.api.model.ApiRequest;
import com.fastub.api.model.ResponseHeader;
import com.fastub.api.service.APIService;

@CrossOrigin
@RestController
public class APIController {

	private static final Logger logger = LogManager.getLogger(APIController.class);

	@Autowired
	private APIService apiService;

	@PostMapping("/fastub/api")
	public ApiRequest saveAPI(@RequestBody ApiDto api) {
	

		logger.info("Executing saveAPI() method, passing request to APIService");

		return apiService.saveAPI(api);

	}

	@GetMapping("/fastub/api")
	public List<ApiRequest> getAllApi() {

		return apiService.getAllApi();

	}

	@PutMapping("/fastub/api/{apiId}")
	public List<ApiResponseDto> updateAPI(@PathVariable("apiId") Integer apiId, @RequestBody ApiDto apiDto) {

		return apiService.updateAPI(apiId, apiDto);

	}

	@DeleteMapping("/fastub/api/{apiId}")
	public Map<String, String> deleteApi(@PathVariable("apiId") Integer apiId) {

		return apiService.deleteApi(apiId);

	}

	@GetMapping("/fastub/api/{apiId}/api-response")
	public ApiDto getApiResponse(@PathVariable("apiId") Integer apiId) {

		return apiService.getApiResponse(apiId);

	}

	@GetMapping("/fastub/api/{apiId}/response-headers")
	public List<ResponseHeader> getResponseHeaders(@PathVariable("apiId") Integer apiId) {

		return apiService.getResponseHeaders(apiId);

	}

	@GetMapping("/fastub/api/meta-data")
	public List<HttpStatusCodeDto> getMetaData() {

		logger.info("Method execution starts getMetaData()");

		List<HttpStatusCodeDto> httpStatusCodeDtos = new ArrayList<HttpStatusCodeDto>();

		for (HttpStatus httpStatus : HttpStatus.values()) {

			HttpStatusCodeDto httpStatusCodeDto = new HttpStatusCodeDto();
			httpStatusCodeDto.setCode(httpStatus.value());
			httpStatusCodeDto.setName(httpStatus.value() + "-" + httpStatus.getReasonPhrase());
			httpStatusCodeDtos.add(httpStatusCodeDto);
		}

		logger.info("Method execution finished getMetaData()");

		return httpStatusCodeDtos;

	}

	@PutMapping("/fastub/api/{apiId}/response-headers")
	public List<ResponseHeader> saveHeaders(@PathVariable("apiId") Integer apiId,
			@RequestBody ResponseHeaderDto responseHeaderDto) {

		return apiService.saveHeaders(apiId, responseHeaderDto);
	}

	@GetMapping("/fastub/api/search")
	public List<ApiRequest> searchApi(@RequestParam("keyword") String keyword) {

		return apiService.searchApi(keyword);

	}

	@DeleteMapping("/fastub/routes")
	public Map<String, Object> deleteAllRoutes() {

		apiService.deleteAllRoutes();

		HashMap<String, Object> response = new HashMap<String, Object>();

		response.put("message", "Routes deleted sucessfully.");

		return response;
	}

}
