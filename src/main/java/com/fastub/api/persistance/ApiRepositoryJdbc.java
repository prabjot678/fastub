package com.fastub.api.persistance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fastub.api.model.ApiRequest;

@Repository
public class ApiRepositoryJdbc {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<ApiRequest> getApiByUrlMatch(String url, String modifiedUrl, String httpMethod) {

		String sql = "SELECT * FROM API_REQUEST where (url=? or url like ?) AND http_method=?";
		
		List<ApiRequest> apiRequests = jdbcTemplate.query(sql, new Object[] { url,modifiedUrl,httpMethod },
				new BeanPropertyRowMapper<ApiRequest>(ApiRequest.class));
		return apiRequests;
	}

}
