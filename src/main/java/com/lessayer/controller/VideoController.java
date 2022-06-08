package com.lessayer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.lessayer.entity.Video;
import com.lessayer.service.VideoService;


@Controller
public class VideoController {
	
	@Autowired
	private VideoService videoService;
	
	@GetMapping("/video/videos")
	public String showVideoPage(Model model, String keyword) {
		return showVideoPageByPage(0, model, keyword);
	}
	
	@GetMapping("/video/videos/{pageNum}")
	public String showVideoPageByPage(@PathVariable("pageNum") Integer currentPage, Model model,
		String keyword) {
		Page<Video> page;
		
		page = listVideoPage(currentPage, keyword);
		
		Integer totalPages = page.getTotalPages();
		Integer prevPage = (currentPage - 1 >= 0) ? currentPage - 1 : 0;
		Integer nextPage = (currentPage + 1 < totalPages) ? currentPage + 1 : totalPages - 1;
		
		model.addAttribute("videoList", page.getContent());
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("prevPage", prevPage);
		model.addAttribute("nextPage", nextPage);
		model.addAttribute("keyword", keyword);
		model.addAttribute("baseURL", "/video/videos");
		
		return "/video/videos";
	}
	
	private Page<Video> listVideoPage(Integer currentPage, String keyword) {
		if (keyword == null) {
			return videoService.listVideoByPage(currentPage);
		}
		else {
			return videoService.listVideoWithKeywordByPage(currentPage, keyword);
		}
	}
}
