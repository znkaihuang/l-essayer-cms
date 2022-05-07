package com.lessayer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.lessayer.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/user/staffs")
	public String showUserPage() {
		return "users";
	}
	
	public String showUserByPage() {
		return "user";
	}
	
	public String createUser() {
		return "user";
	}
	
}
