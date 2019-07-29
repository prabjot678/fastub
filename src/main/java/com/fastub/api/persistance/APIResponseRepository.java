package com.fastub.api.persistance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastub.api.model.ApiResponse;

@Repository
public interface APIResponseRepository extends JpaRepository<ApiResponse, Integer> {

	List<ApiResponse> findByApiRequestId(Integer id);

}
