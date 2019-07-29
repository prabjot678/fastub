package com.fastub.api.persistance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastub.api.model.ResponseHeader;

@Repository
public interface ResponseHeaderRepository extends JpaRepository<ResponseHeader, Integer> {
	
	List<ResponseHeader> findByApiRequestId(Integer id);

}
