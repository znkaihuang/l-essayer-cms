package com.lessayer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
	
	@GetMapping("/user/staffs")
	public String showUserPage() {
		return "about";
	}
	
	public String showUserByPage() {
		return "user";
	}
	
	public String createUser() {
		return "user";
	}
	
}
