package com.lessayer.controller;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lessayer.FileUploadUtil;
import com.lessayer.entity.Language;
import com.lessayer.entity.Lecturer;
import com.lessayer.entity.Tag;
import com.lessayer.entity.Video;
import com.lessayer.filter.FilterHelper;
import com.lessayer.service.TagService;
import com.lessayer.service.VideoService;


@Controller
public class VideoController {
	
	@Autowired
	private VideoService videoService;
	
	@Autowired
	private TagService tagService;
	
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
		
		page = listVideoPage(currentPage, keyword, filterSelect);
		
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
		model.addAttribute("suffixURL", returnKeywordAndFilterSelectSuffixURL(keyword, filterSelect));
		
		return "/video/videos";
	}
	
	@GetMapping("/video/videos/create")
	public String showCreateVideoPage(Model model) {
		String pageTitle;
		List<Tag> tagList = tagService.retrieveAllTags();
		
		Video video = new Video();
		Lecturer lecturer = new Lecturer();
		
		if (video.getId() == null) {
			pageTitle = "Upload New Video";
		}
		else {
			pageTitle = "Edit Video ID " + video.getId();
		}
		
		model.addAttribute("video", video);
		model.addAttribute("lecturer", lecturer);
		model.addAttribute("tagList", tagList);
		model.addAttribute("pageTitle", pageTitle);
		model.addAttribute("returnPage", 0);
		model.addAttribute("baseURL", "/video/videos");
		model.addAttribute("suffixURL", returnKeywordAndFilterSelectSuffixURL(null, null));
		model.addAttribute("supportedLanguages", Language.values());
		
		return "/video/video_form";
	}
	
	@PostMapping("/video/videos/save/{pageNum}")
	public String saveVideo(@PathVariable("pageNum") Integer pageNum, Video video, Lecturer lecturer, 
		RedirectAttributes redirectAttributes, String keyword, String filterSelect, String tag, String date,
		MultipartFile uploadedVideo, MultipartFile uploadedImage) throws IOException, ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		video.setDate(dateFormat.parse(date));
		updateTags(video, parseTagString(tag));
		
		Optional<Lecturer> lecturerOptional = videoService.findLecturerByName(lecturer);
		if (lecturerOptional.isEmpty()) {
			Lecturer savedLecturer = videoService.saveLecturer(lecturer);
			video.setLecturer(savedLecturer);
		}
		else {
			video.setLecturer(lecturerOptional.get());
		}
		
		if (!uploadedVideo.isEmpty()) {
			video.setUrl(uploadedVideo.getOriginalFilename());
		}
		
		if (!uploadedImage.isEmpty()) {
			video.setCoverImage(uploadedImage.getOriginalFilename());
		}
		
		Video savedVideo = videoService.saveVideo(video);
		
		if (!uploadedVideo.isEmpty()) {
			uploadedFile(savedVideo.getId(), uploadedVideo, "video");
		}
		
		if (!uploadedImage.isEmpty()) {
			uploadedFile(savedVideo.getId(), uploadedImage, "image");
		}

		String modalBody = (video.getId() == null) ? "Create Video with ID " + savedVideo.getId() : "Update Video with ID " + video.getId();
		setModalMessage(redirectAttributes, "Success", modalBody);
		
		return formatRedirectURL("redirect:/video/videos/" + pageNum, keyword, filterSelect);
	}
	
	private void setModalMessage(RedirectAttributes redirectAttributes, String modalTitle, String modalBody) {
		redirectAttributes.addFlashAttribute("modalTitle", modalTitle);
		redirectAttributes.addFlashAttribute("modalBody", modalBody);
	}
	
	private void uploadedFile(Integer videoId, MultipartFile file, String type) throws IOException {
		String uploadDir = "videos/" + videoId + "/" + type;
		FileUploadUtil.remove(uploadDir);
		FileUploadUtil.saveFile(uploadDir, file.getOriginalFilename(), file, type.equals("video"));
	}

	private String formatRedirectURL(String redirectURL, String keyword, String filterSelect) throws UnsupportedEncodingException {
		return redirectURL +  URLEncoder.encode(returnKeywordAndFilterSelectSuffixURL(keyword, filterSelect), "utf-8");
	}

	private void updateTags(Video video, List<Tag> updatedTags) {
		List<Tag> tagCacheList = tagService.retrieveAllTags();
		Set<Tag> newTags = new TreeSet<>();
		updatedTags.forEach(updatedTag -> {
			if (tagCacheList.contains(updatedTag)) {
				Integer index = tagCacheList.indexOf(updatedTag);
				newTags.add(tagCacheList.get(index));
			}
			else {
				Tag tagInDB = tagService.createTag(updatedTag);
				tagCacheList.add(tagInDB);
				newTags.add(tagInDB);
			}
		});
		video.setTags(newTags);
	}
	
	// The format of tag string is "<tag>,<tag>, ...
	private List<Tag> parseTagString(String tagString) {
		List<Tag> tagList = new ArrayList<>();
		String[] tagStringArray = tagString.split(",");
		
		for (int count = 0; count < tagStringArray.length; count++) {
			tagList.add(new Tag(tagStringArray[count]));
		}
		
		return tagList;
	}
	
	private String returnKeywordAndFilterSelectSuffixURL(String keyword, String filterSelect) {
		if (filterSelect != null) {
			String filterSelectSuffix = filterSelect.replace(",", "&filterSelect=");
			if (keyword == null) {
				return "?filterSelect=" + filterSelectSuffix;
			}
			else {
				return "?keyword=" + keyword + "&filterSelect=" + filterSelectSuffix;
			}
		}
		else {
			if (keyword == null) {
				return "";
			}
			else {
				return "?keyword=" + keyword;
			}
		}
	}
	
	private Page<Video> listVideoPage(Integer currentPage, String keyword, String filterSelect) {
		if (keyword == null && filterSelect == null) {
			return videoService.listVideosByPage(currentPage);
		}
		else if(keyword != null && filterSelect == null) {
			return videoService.listVideosWithKeywordByPage(currentPage, keyword);
		}
		else if(keyword == null && filterSelect != null) {
			return videoService.listVideosWithFilterByPage(currentPage, filterHelper.getFilterQueryList());
		}
		else {
			return videoService.listVideosWithKeywordAndFilterByPage(currentPage, keyword, filterHelper.getFilterQueryList());
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
