package com.lessayer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class VideoController {
	
	@GetMapping("/video/videos")
	public String showVideoPage(Model model, String keyword) {
		return showVideoPageByPage(0, model, keyword);
	}
	
	@GetMapping("/video/videos/{pageNum}")
	public String showVideoPageByPage(@PathVariable("pageNum") Integer currentPage, Model model,
		String keyword) {
//		Page<Article> page;
//		
//		page = listArticlePage(currentPage, keyword);
//		
//		Integer totalPages = page.getTotalPages();
//		Integer prevPage = (currentPage - 1 >= 0) ? currentPage - 1 : 0;
//		Integer nextPage = (currentPage + 1 < totalPages) ? currentPage + 1 : totalPages - 1;
//		
//		model.addAttribute("articleList", page.getContent());
//		model.addAttribute("totalPages", totalPages);
//		model.addAttribute("currentPage", currentPage);
//		model.addAttribute("prevPage", prevPage);
//		model.addAttribute("nextPage", nextPage);
		model.addAttribute("keyword", keyword);
		model.addAttribute("baseURL", "/article/articles");
		
		return "/video/videos";
	}
	
}
