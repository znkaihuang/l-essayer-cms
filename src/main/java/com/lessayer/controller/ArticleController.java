package com.lessayer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArticleController {
	
	@GetMapping("/article/articles")
	public String showArticlePage() {
		return "/article/articles";
	}
	
}
