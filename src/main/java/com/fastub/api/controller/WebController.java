package com.fastub.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
	
	/*
	 * @GetMapping("/api-list") public String main(Model model) {
	 * 
	 * 
	 * return "api-listing"; //view }
	 */
	 
	 @GetMapping("/")
	 public String index(Model model) {            
	     return "api-listing";
	 } 

}
