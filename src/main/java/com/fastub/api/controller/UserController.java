package com.fastub.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fastub.api.model.User;
import com.fastub.api.service.UserService;


@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/superman/account/create")
	User createAccount(@RequestBody User user) {

		return userService.createAccount(user);

	}

	@PostMapping("/superman/account/login")
	User login(@RequestBody User user) {

		return userService.login(user.getUsername(), user.getPassword());
	}

	@DeleteMapping("{userId}")
	User deleteAccount(@PathVariable("userId") String userId) {

		return null;
	}

}
