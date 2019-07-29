package com.fastub.api.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StaticController {
	
	@Autowired
	ServletContext servletContext;
	
	@RequestMapping(value = "/api-listing.css", method = RequestMethod.GET)
	public ResponseEntity css(HttpServletResponse response) throws IOException {

	    // Set the content-type
	    response.setHeader("Content-Type", "text/css");

	    
	    InputStream is = new ClassPathResource("/templates/static/css/api-listing.css").getInputStream();
	    

        IOUtils.copy(is, response.getOutputStream());
        IOUtils.closeQuietly(is);

	    response.flushBuffer();

	    return new ResponseEntity(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/api-listing.js", method = RequestMethod.GET)
	public ResponseEntity js(HttpServletResponse response) throws IOException {

	    

		InputStream is = new ClassPathResource("/templates/static/js/api-listing.js").getInputStream();
	    
        IOUtils.copy(is, response.getOutputStream());
        IOUtils.closeQuietly(is);

	    response.flushBuffer();

	    return new ResponseEntity(HttpStatus.OK);
	}

}
