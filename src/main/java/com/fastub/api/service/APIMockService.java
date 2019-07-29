package com.fastub.api.service;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastub.api.model.ApiRequest;
import com.fastub.api.persistance.APIRepository;

@Service
public class APIMockService {

	private static final Logger logger = LogManager.getLogger(APIMockService.class);

	@Autowired
	private APIRepository apiRepository;

	public Optional<ApiRequest> getMockResponse(String url, String httpMethod) {

		logger.info("Request for mock response for URL " + url + " Http Method " + httpMethod);

		ApiRequest apiRequest = apiRepository.findByUrlAndHttpMethod(url, httpMethod);

		if (apiRequest == null) {

			logger.info("API does not exist in the database dir for the same URL " + url);

			int lenghtOfUrl = 0;
			try {
				lenghtOfUrl = StringUtils.countMatches(URLDecoder.decode(url, "UTF-8"), "/");
				logger.info("Length of the URL " + lenghtOfUrl);
			} catch (Exception exception) {
				logger.error("Exception occured while counting slashes in URL " + url);
				logger.error(exception);
			}

			List<ApiRequest> apiRequests = apiRepository
					.findByUrlLengthAndHttpMethodAndParamPositionNotNull(lenghtOfUrl, httpMethod);

			for (ApiRequest request : apiRequests) {

				List<Integer> paramPositions = getParamPositions(request.getParamPosition());

				String dbUrl = replaceUrlByIndex(paramPositions, request.getUrl());

				logger.info("DB URL " + url);

				String requestUrl = replaceUrlByIndex(paramPositions, url);

				logger.info("Request url " + url);

				if (dbUrl.equals(requestUrl)) {
					logger.info("URL matched ");
					apiRequest = request;
				}

			}

		}

		return Optional.ofNullable(apiRequest);
	}

	private List<Integer> getParamPositions(String paramPosition) {

		logger.info("Algorithm starts for findings param postions for mock response, param postion before split  "
				+ paramPosition);

		String positons[] = paramPosition.split(",");

		logger.info("Param position after split " + positons);

		List<Integer> positonsAsInteger = new ArrayList<Integer>();
		for (String position : positons) {
			if (StringUtils.isNotBlank(position)) {
				positonsAsInteger.add(Integer.parseInt(position));
			}

		}

		return positonsAsInteger;
	}

	public String replaceUrlByIndex(List<Integer> paramPostions, String url) {

		String[] urlContent = url.split("/");

		String modifiedUrl = "/";

		int index = 1;
		for (String content : urlContent) {

			if (!paramPostions.contains(index)) {

				if (modifiedUrl.equals("/")) {
					modifiedUrl = modifiedUrl + content;
				} else {
					modifiedUrl = modifiedUrl + "/" + content;
				}
			}
			if (StringUtils.isNotBlank(content)) {
				index++;
			}

		}

		return modifiedUrl;
	}

}
