package com.lessayer.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.lessayer.entity.Video;
import com.lessayer.filter.FilterHelper;
import com.lessayer.service.VideoService;


@Controller
public class VideoController {
	
	@Autowired
	private VideoService videoService;
	
	private FilterHelper filterHelper;
	
	@GetMapping("/video/videos")
	public String showVideoPage(Model model, String keyword) {
		constructFilter();
		filterHelper.setPrevFilterSelect("0-0");
		
		return showVideoPageByPage(0, model, keyword, null);
	}
	
	@GetMapping("/video/videos/{pageNum}")
	public String showVideoPageByPage(@PathVariable("pageNum") Integer currentPage, Model model,
		String keyword, String filterSelect) {
		
		Page<Video> page;
		
		if (filterSelect != null) {
			filterHelper.updateFilter(filterSelect);
			filterHelper.setPrevFilterSelect(filterSelect);
		}
		else {
			filterHelper.clearFilter();
		}
		
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
		model.addAttribute("filter", filterHelper.getFilter());
		
		return "/video/videos";
	}
	
	private Page<Video> listVideoPage(Integer currentPage, String keyword) {
		if (keyword == null) {
			return videoService.listVideosByPage(currentPage);
		}
		else {
			return videoService.listVideosWithKeywordByPage(currentPage, keyword);
		}
	}
	
	private void constructFilter() {
		String[] sections = {"Language", "Video Length", "Subtitle"};
		String[] options = {
				"English", "French", "Chinese",
				"0-10 minutes", "11-30 minutes", "31-60 minutes", "More than 1 hour",
				"With subtitle", "Without subtitle"
		};
		Integer[] optionNumPerSection = {3, 4, 2};
		
		filterHelper = new FilterHelper(sections, options, optionNumPerSection);
	}
	
}
