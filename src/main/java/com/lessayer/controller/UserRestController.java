package com.lessayer.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lessayer.entity.User;
import com.lessayer.entity.UserDTO;
import com.lessayer.service.UserService;


@RestController
public class UserRestController {
	
	@Autowired
	private UserService userService;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@PostMapping("/user/{userType}/create/checkEmailUnique")
	public Boolean checkEmailUnique(@PathVariable("userType") String userType, @RequestBody String responseBody) 
			throws JsonMappingException, JsonProcessingException {
		UserDTO userDTO = objectMapper.readValue(responseBody, UserDTO.class);
		Optional<User> userOptional = userService.findUserByEmail(userDTO.getEmail());

		return (userOptional.isEmpty()) ? true : false;
	}
	
}
