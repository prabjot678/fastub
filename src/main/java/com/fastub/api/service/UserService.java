package com.fastub.api.service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fastub.api.model.User;
import com.fastub.api.persistance.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User createAccount(User user) {

		return userRepository.save(user);

	}

	public User login(String username, String password) {

		User user = userRepository.findByUsername(username);

		if (user == null) {

			throw new EntityNotFoundException("Invalid username or password.");
		} else if (!password.equals(user.getPassword())) {
			throw new EntityNotFoundException("Invalid username or password.");
		} else {
			return user;

		}

	}
	
	public Optional<User> getUser(Integer id) {
		
		return userRepository.findById(id);
	}

}