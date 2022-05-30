package com.lessayer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArticleController {
	
	@GetMapping("/article/articles")
	public String showArticlePage(Model model) {
		model.addAttribute("baseURL", "/article/articles");
		
		return "/article/articles";
	}
	
}
