package com.fastub.api.persistance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastub.api.model.ApiRequest;

@Repository
public interface APIRepository extends JpaRepository<ApiRequest, Integer> {

	
	
	ApiRequest findByUrlAndHttpMethod(String url,String httpMethod);
	
	List<ApiRequest> findByUrlLengthAndHttpMethodAndParamPositionNotNull(int lenght,String httpMethod);
	
	
	List<ApiRequest> findByUrlContainingOrderByCreatedAtDesc(String url);
	
	List<ApiRequest> findAllByOrderByCreatedAtDesc();

}
