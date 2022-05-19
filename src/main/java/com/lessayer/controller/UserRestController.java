package com.lessayer.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lessayer.entity.User;
import com.lessayer.service.UserService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RestController
public class UserRestController {
	
	@Autowired
	private UserService userService;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@PostMapping("/user/staffs/createStaff/checkEmailUnique")
	public Boolean checkEmailUnique(@RequestBody String responseBody) throws JsonMappingException, JsonProcessingException {
		
		UserDTO userDTO = objectMapper.readValue(responseBody, UserDTO.class);
		Optional<User> userOptional = userService.findUserByEmail(userDTO.getEmail());
		System.out.println(responseBody + " " + userOptional.isEmpty() + " " + userDTO.userId + " " + userDTO.email);
		return (userOptional.isEmpty()) ? true : false;
	}
	
}

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
class UserDTO {
	Integer userId;
	String email;
}
