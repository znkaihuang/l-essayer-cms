package com.lessayer;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
	
	@GetMapping("/")
	public String showHomePage() {		
		return "index";
	}
	
	@GetMapping("/login")
	public String showLoginPage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
		}
		
		return "redirect:/";
	}
	
	@GetMapping("/favicon.ico")
    @ResponseBody
    public void returnNoFavicon() {
    }
	
}
