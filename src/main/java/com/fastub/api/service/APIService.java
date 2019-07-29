package com.fastub.api.service;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastub.api.dto.ApiDto;
import com.fastub.api.dto.ApiResponseDto;
import com.fastub.api.dto.ResponseHeaderDto;
import com.fastub.api.exception.DuplicateDataException;
import com.fastub.api.exception.EntityNotFoundException;
import com.fastub.api.model.ApiRequest;
import com.fastub.api.model.ApiResponse;
import com.fastub.api.model.ResponseHeader;
import com.fastub.api.persistance.APIRepository;
import com.fastub.api.persistance.APIResponseRepository;
import com.fastub.api.persistance.ApiRepositoryJdbc;
import com.fastub.api.persistance.ResponseHeaderRepository;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@Service
public class APIService {

	private static final Logger logger = LogManager.getLogger(APIService.class);

	@Autowired
	private APIRepository apiRepository;

	@Autowired
	private APIResponseRepository apiResponseRepository;

	@Autowired
	private ApiRepositoryJdbc apiRepositoryJdbc;

	@Autowired
	private ResponseHeaderRepository responseHeaderRepository;

	public ApiRequest saveAPI(ApiDto apiDto) {

		ApiRequest api = new ApiRequest();

		String url = apiDto.getUrl();

		if (url.endsWith("/")) {
			logger.info("Before removing slash at end of the URL " + url);
			url = url.substring(0, url.length() - 1);
			logger.info("After removing slash at end of the URL " + url);
		}

		if (!url.startsWith("/")) {
			logger.info("Before adding backward slash at start of the URL " + url);
			url = "/" + url;
			logger.info("After adding backward slash at start of the URL " + url);
		}

		int lenghtOfUrl = 0;
		try {
			lenghtOfUrl = StringUtils.countMatches(URLDecoder.decode(url, "UTF-8"), "/");
			logger.info("Length of the URL " + lenghtOfUrl);
		} catch (Exception exception) {
			logger.error("Exception occured while counting slashes in URL " + url);
			logger.error(exception);
		}

		api.setUrlLength(lenghtOfUrl);

		if (url.contains("{")) {

			String paramPosition = getParamPosition(url);

			logger.info("URL contains params, params at position " + paramPosition);

			api.setParamPosition(paramPosition);
		}

		String modifiedUrl;

		if (url.contains("{")) {

			modifiedUrl = url.replaceAll("\\{(.*?)}", "{%%}");

		} else {
			modifiedUrl = apiDto.getUrl();
		}

		List<ApiRequest> existingUrls = apiRepositoryJdbc.getApiByUrlMatch(url, modifiedUrl, apiDto.getHttpMethod());

		if (!existingUrls.isEmpty()) {

			logger.info("Duplicate URL ", url);

			throw new DuplicateDataException("URI already exist.");
		}

		api.setUrl(url);

		api.setHttpMethod(apiDto.getHttpMethod());

		return apiRepository.save(api);
	}

	public List<ApiRequest> getAllApi() {

		logger.info("Executing getAllApi() method to return all api urls");

		return apiRepository.findAllByOrderByCreatedAtDesc();
	}

	public List<ApiResponseDto> getApiResponse(Integer apiId) {

		logger.info("Returning api response for API ID " + apiId);

		ApiRequest apiRequest = new ApiRequest();
		apiRequest.setId(apiId);

		List<ApiResponse> apiResponses = apiResponseRepository.findByApiRequestId(apiId);

		return getResponseAsJson(apiResponses);

	}

	private List<ApiResponseDto> getResponseAsJson(List<ApiResponse> apiResponses) {

		ObjectMapper objectMapper = new ObjectMapper();
		List<ApiResponseDto> apiResponseDtos = new ArrayList<ApiResponseDto>();
		for (ApiResponse apiResponse : apiResponses) {

			ApiResponseDto apiResponseDto = new ApiResponseDto();
			apiResponseDto.setId(apiResponse.getId());
			apiResponseDto.setActive(apiResponse.isActive());
			apiResponseDto.setStatusCode(apiResponse.getStatusCode());
			

			if (StringUtils.isNotBlank(apiResponse.getResponse())) {
				
				JsonElement element;
				try {
					
					element = new JsonParser().parse(apiResponse.getResponse()).getAsJsonObject();
				} catch (Exception exception) {
					element = new JsonParser().parse(apiResponse.getResponse()).getAsJsonArray();
				}
				

				try {
					
					if(element.isJsonArray()) {
						List<Map<String,Object>> response = objectMapper.readValue(element.toString(),
								new TypeReference<List<Map<String, Object>>>() {
								});
						
						
						apiResponseDto.setResponse(response);
					} else {
						Map<String, Object> response = objectMapper.readValue(element.toString(),
								new TypeReference<Map<String, Object>>() {
								});
						
						
						apiResponseDto.setResponse1(response);
					}
					
				} catch (JsonParseException jsonParseException) {
					logger.error("JsonParseException occured while parsing the API response string to JSON format ",
							jsonParseException);
					logger.error("JSON response string for parsing => " + element.toString());

				} catch (Exception exception) {
					logger.error("Exception occured while parsing the API response string to JSON format ", exception);
					logger.error("JSON response string for parsing => " + element.toString());
				}
			}

			apiResponseDtos.add(apiResponseDto);

		}

		return apiResponseDtos;

	}

	public List<ResponseHeader> getResponseHeaders(Integer id) {

		return responseHeaderRepository.findByApiRequestId(id);
	}

	public List<ApiResponseDto> updateAPI(Integer apiId, ApiDto apiDto) {

		logger.info("Update API request for APi ID ", apiId);

		Optional<ApiRequest> api = apiRepository.findById(apiId);

		if (!api.isPresent()) {

			logger.error("Invalid API id ", apiId);

			throw new EntityNotFoundException("Invalid API ID.");

		}

		if (!api.get().getHttpMethod().equalsIgnoreCase(apiDto.getHttpMethod())) {

			if (!uriIsUnique(api.get().getUrl(), apiDto.getHttpMethod())) {

				logger.info("Duplicate URL ", api.get().getUrl());

				throw new DuplicateDataException("URI already exist.");
			}

		}

		List<ApiResponseDto> responseDto = apiDto.getResponseDtos();

		List<ApiResponse> existingApiResponse = api.get().getApiResponse();

		if (existingApiResponse == null) {
			existingApiResponse = new ArrayList<ApiResponse>();
		}

		ApiRequest apiToUpdate = api.get();

		apiToUpdate.setHttpMethod(apiDto.getHttpMethod());

		List<ApiResponse> apiResponses = new ArrayList<ApiResponse>();

		for (ApiResponseDto apiResponseDto : responseDto) {

			ApiResponse objToUpdate;

			if (apiResponseDto.getId() != null) {
				Optional<ApiResponse> result = existingApiResponse.stream()
						.filter(dbObj -> dbObj.getId().equals(apiResponseDto.getId())).findFirst();
				if (result.isPresent()) {

					objToUpdate = result.get();

				} else {
					objToUpdate = new ApiResponse();
				}
			} else {
				objToUpdate = new ApiResponse();
			}

			objToUpdate.setActive(apiResponseDto.isActive());

			if (apiResponseDto.getResponse() != null) {
				Gson gson = new Gson();
				String json = gson.toJson(apiResponseDto.getResponse());

				objToUpdate.setResponse(json);
			}
			
			if(apiResponseDto.getResponse1() != null) {
				Gson gson = new Gson();
				String json = gson.toJson(apiResponseDto.getResponse1());

				objToUpdate.setResponse(json);
			}

			objToUpdate.setApiRequest(apiToUpdate);
			objToUpdate.setStatusCode(apiResponseDto.getStatusCode());
			apiResponses.add(objToUpdate);
		}

		apiToUpdate.setApiResponse(apiResponses);

		apiRepository.save(apiToUpdate);

		return getResponseAsJson(apiResponses);

	}

	public Map<String, String> deleteApi(Integer id) {

		logger.info("Request for delete API, API ID " + id);

		if (id == null) {
			logger.error("API cannot be deleted becaue of API is not present " + id);
			throw new EntityNotFoundException("Invalid id.");
		}

		Optional<ApiRequest> apiRequest = apiRepository.findById(id);
		if (!apiRequest.isPresent()) {
			logger.error("API cannot be deleted becaue of API is not present " + id);

			throw new EntityNotFoundException("Api not found.");
		}

		apiRepository.deleteById(id);

		logger.info("API deleted successfully.");

		Map<String, String> response = new HashMap<String, String>();
		response.put("message", "Success");
		return response;
	}

	private String getParamPosition(String url) {

		logger.info("Algorithm executing for calculating path param position");

		int position = 0;
		String positionOfParams = "";
		for (int i = 0; i < url.length(); i++) {
			if (Character.toString(url.charAt(i)).equals("/")) {
				position++;
			}

			if (Character.toString(url.charAt(i)).equals("{") || Character.toString(url.charAt(i)).equals(":")) {

				if (positionOfParams.isEmpty()) {
					positionOfParams = position + "";
				} else {
					positionOfParams = positionOfParams + "," + position;
				}
			}

		}

		logger.info("Algorithm finished for calculating path param position, param position " + " url " + url);

		return positionOfParams;

	}

	public List<ResponseHeader> saveHeaders(Integer apiId, ResponseHeaderDto responseHeaderDto) {

		logger.info("Request for saving headers for API ID " + apiId);

		Optional<ApiRequest> api = apiRepository.findById(apiId);

		if (!api.isPresent()) {

			logger.error("API not found , Invalid API ID " + apiId);

			throw new EntityNotFoundException("Invalid API ID.");

		}
		ApiRequest apiToUpdate = api.get();
		// update header

		List<ResponseHeader> responseHeaders = api.get().getResponseHeaders();

		ResponseHeader responseHeaderObjToUpdate = null;

		List<ResponseHeader> responseHeadersToUpdate = new ArrayList<ResponseHeader>();

		for (ResponseHeader dtoResponseHeaderObj : responseHeaderDto.getResponseHeaders()) {

			Optional<ResponseHeader> result = responseHeaders.stream()
					.filter(dbObj -> dbObj.getId().equals(dtoResponseHeaderObj.getId())).findAny();

			responseHeaderObjToUpdate = result.isPresent() ? result.get() : new ResponseHeader();

			responseHeaderObjToUpdate.setKey(dtoResponseHeaderObj.getKey());
			responseHeaderObjToUpdate.setValue(dtoResponseHeaderObj.getValue());
			responseHeaderObjToUpdate.setApiRequest(apiToUpdate);
			responseHeadersToUpdate.add(responseHeaderObjToUpdate);
		}

		apiToUpdate.setResponseHeaders(responseHeadersToUpdate);

		responseHeaderRepository.deleteAll();

		apiRepository.save(apiToUpdate);

		return apiToUpdate.getResponseHeaders();
	}

	private boolean uriIsUnique(String url, String httpMethod) {

		logger.info("Running algorithm for checking the uniquness of the URL.");

		String modifiedUrl;

		if (url.contains("{")) {

			modifiedUrl = url.replaceAll("\\{(.*?)}", "{%%}");

		} else {
			modifiedUrl = url;
		}

		List<ApiRequest> existingUrls = apiRepositoryJdbc.getApiByUrlMatch(url, modifiedUrl, httpMethod);

		return existingUrls.isEmpty();

	}

	public List<ApiRequest> searchApi(String keyword) {

		logger.info("Request for search API , keyword enterted " + keyword);

		List<ApiRequest> apiRequests = apiRepository.findByUrlContainingOrderByCreatedAtDesc(keyword);

		logger.info("Number of records found for matching keyword " + apiRequests.size());

		return apiRequests;

	}

}
