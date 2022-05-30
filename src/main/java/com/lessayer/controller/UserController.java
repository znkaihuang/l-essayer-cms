package com.lessayer.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lessayer.AbstractFileExporter;
import com.lessayer.FileUploadUtil;
import com.lessayer.entity.Role;
import com.lessayer.entity.User;
import com.lessayer.exporter.UserCsvExporterDelegate;
import com.lessayer.exporter.UserPdfExporterDelegate;
import com.lessayer.service.RoleService;
import com.lessayer.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@GetMapping("/user/{userType}")
	public String showUserPage(@PathVariable("userType") String userType, Model model, String keyword) {
		return showUserPageByPage(userType, 0, model, keyword);
	}
	
	@GetMapping("/user/{userType}/{pageNum}")
	public String showUserPageByPage(@PathVariable("userType") String userType,
			@PathVariable("pageNum") Integer currentPage, Model model,
		String keyword) {
		Page<User> page;
		
		page = listUserPage(userType, currentPage, keyword);
		
		Integer totalPages = page.getTotalPages();
		Integer prevPage = (currentPage - 1 >= 0) ? currentPage - 1 : 0;
		Integer nextPage = (currentPage + 1 < totalPages) ? currentPage + 1 : totalPages - 1;
		
		model.addAttribute("userList", page.getContent());
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("prevPage", prevPage);
		model.addAttribute("nextPage", nextPage);
		model.addAttribute("keyword", keyword);
		model.addAttribute("baseURL", "/user/" + userType);
		model.addAttribute("userType", userType);
		return "/user/users";
	}

	@GetMapping("/user/{userType}/create")
	public String showCreateUserPage(@PathVariable("userType") String userType, Model model) {
		String pageTitle;
		List<Role> roleList;
		
		if (userType.equals("staffs")) {
			roleList = roleService.findStaffs();
			pageTitle = "Create New Staff";
		}
		else {
			roleList = roleService.findSubscribers();
			pageTitle = "Create New Subscriber";
		}
		
		User user = new User();
		user.setPhotos("/images/user-solid.svg");
		
		model.addAttribute("roleList", roleList);
		model.addAttribute("user", user);
		model.addAttribute("pageTitle", pageTitle);
		model.addAttribute("returnPage", 0);
		model.addAttribute("baseURL", "/user/" + userType);
		model.addAttribute("userType", userType);
		return "/user/user_form";
	}
	
	@PostMapping("/user/{userType}/save/{pageNum}")
	public String saveUser(@PathVariable("userType") String userType, @PathVariable("pageNum") Integer pageNum,
			User user, String enabled, String disabled, RedirectAttributes redirectAttributes,
			@RequestParam("imageFile") MultipartFile imageFile, String keyword) throws IOException {
		setUserEnableStatus(enabled, user);
		
		User savedUser;
		if (!imageFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename()); 
			user.setPhotos(fileName);
			savedUser = userService.saveUser(user);
			uploadPhoto(savedUser.getId(), fileName, imageFile);
		}
		else {
			savedUser = userService.saveUser(user);
		}
		
		String modalBody = (user.getId() == null) ? "Create User with ID " + savedUser.getId() : "Update User with ID " + user.getId();
		setModalMessage(redirectAttributes, "Success", modalBody);
		
		return formatRedirectURL("redirect:/user/" + userType + "/" + pageNum, keyword);
	}
	
	@GetMapping("/user/{userType}/edit/{pageNum}/{userId}/{showId}")
	public String editUser(@PathVariable("userType") String userType, @PathVariable("pageNum") Integer pageNum, 
		@PathVariable("userId") Integer userId, Model model, RedirectAttributes redirectAttributes,
		String keyword) {
		Optional<User> userOptional = userService.findUserById(userId);
		
		if (userOptional.isEmpty()) {
			setModalMessage(redirectAttributes, "Error", "Cannot find User with ID " + userId);
			return "redirect:/user/" + userType;
		}
		else {
			String pageTitle;
			List<Role> roleList;
			
			if (userType.equals("staffs")) {
				roleList = roleService.findStaffs();
				pageTitle = "Edit Staff ID " + userId;
			}
			else {
				roleList = roleService.findSubscribers();
				pageTitle = "Edit Subscriber ID " + userId;
			}
			
			model.addAttribute("user", userOptional.get());
			model.addAttribute("roleList", roleList);
			model.addAttribute("pageTitle", pageTitle);
			model.addAttribute("returnPage", pageNum);
			if (keyword != null) {
				model.addAttribute("keyword", keyword);
			}
			model.addAttribute("baseURL", "/user/" + userType);
			model.addAttribute("userType", userType);
			
			return "/user/user_form";
		}
	}
	
	@GetMapping("/user/{userType}/view/{pageNum}/{userId}/{showId}")
	public String viewUser(@PathVariable("userType") String userType, @PathVariable("pageNum") Integer pageNum,
		@PathVariable("userId") Integer userId, @PathVariable("showId") Integer showId,
		RedirectAttributes redirectAttributes, String keyword) {
		Optional<User> userOptional = userService.findUserById(userId);
		
		if (userOptional.isEmpty()) {
			setModalMessage(redirectAttributes, "Error", "Cannot find User with ID " + userId);
		}
		else {
			setModalMessage(redirectAttributes, "User " + userOptional.get().getId(), "");
			redirectAttributes.addFlashAttribute("showId", showId);
		}
		
		return formatRedirectURL("redirect:/user/" + userType + "/" + pageNum, keyword);
	}
	
	@GetMapping("/user/{userType}/requestRemove/{pageNum}/{userId}/{showId}")
	public String requestRemoveUser(@PathVariable("userType") String userType, @PathVariable("pageNum") Integer pageNum,
		@PathVariable("userId") Integer userId, @PathVariable("showId") Integer showId,
		RedirectAttributes redirectAttributes, String keyword) {
		Optional<User> userOptional = userService.findUserById(userId);
		
		if (userOptional.isEmpty()) {
			setModalMessage(redirectAttributes, "Error", "Cannot find User with ID " + userId);
		}
		else {
			setModalMessage(redirectAttributes, "Warning", "Are you sure to delete the user with ID " + userId + "?");
			redirectAttributes.addFlashAttribute("userId", userId);
			redirectAttributes.addFlashAttribute("showId", showId);
			redirectAttributes.addFlashAttribute("yesButtonURL", 
					formatRedirectURL("/user/" + userType + "/remove/" + pageNum + "/" + userId + "/" + showId, keyword));
		}
		
		return formatRedirectURL("redirect:/user/" + userType + "/" + pageNum, keyword);
	}
	
	@GetMapping("/user/{userType}/remove/{pageNum}/{userId}/{showId}")
	public String removeUser(@PathVariable("userType") String userType, @PathVariable("pageNum") Integer pageNum,
			@PathVariable("userId") Integer userId, @PathVariable("showId") Integer showId,
			RedirectAttributes redirectAttributes, String keyword) {
		
		userService.deleteUserById(userId);
		FileUploadUtil.remove("user-photos/" + userId);
		
		if (showId == 0 && pageNum > 0) {
			pageNum--;
		}
		setModalMessage(redirectAttributes, "Success", "Successfully delete user with ID " + userId);
		
		return formatRedirectURL("redirect:/user/" + userType + "/" + pageNum, keyword);
	}
	
	@GetMapping("/user/{userType}/exportCsv")
	public void exportToCsv(@PathVariable("userType") String userType, HttpServletResponse response) 
		throws IOException {
		List<User> userList;
		if (userType.equals("staffs")) {
			userList = userService.listAllStaffs();
		}
		else {
			userList = userService.listAllSubscribers();
		}
		AbstractFileExporter<User> csvExporter = UserCsvExporterDelegate.getCsvExporter();
		csvExporter.export(userList, response);
	}
	
	@GetMapping("/user/{userType}/exportPdf")
	public void exportToPdf(@PathVariable("userType") String userType, HttpServletResponse response)
		throws IOException {
		List<User> userList;
		if (userType.equals("staffs")) {
			userList = userService.listAllStaffs();
		}
		else {
			userList = userService.listAllSubscribers();
		}
		AbstractFileExporter<User> pdfExporter = UserPdfExporterDelegate.getPdfExporter();
		pdfExporter.export(userList, response);
	}
	
	private void uploadPhoto(Integer userId, String fileName, MultipartFile imageFile)
		throws IOException {
		String uploadDir = "user-photos/" + userId;
		FileUploadUtil.remove(uploadDir);
		FileUploadUtil.saveFile(uploadDir, fileName, imageFile);
	}
	
	private void setUserEnableStatus(String enabled, User user) {
		if (enabled.equals("true")) {
			user.setEnabled(true);
		}
		else {
			user.setEnabled(false);
		}
	}
	
	private String formatRedirectURL(String redirectURL, String keyword) {
		return (keyword == null) ? redirectURL : redirectURL + "?keyword=" + keyword;
	}
	
	private void setModalMessage(RedirectAttributes redirectAttributes, String modalTitle, String modalBody) {
		redirectAttributes.addFlashAttribute("modalTitle", modalTitle);
		redirectAttributes.addFlashAttribute("modalBody", modalBody);
	}
	
	private Page<User> listUserPage(String userType, Integer currentPage, String keyword) {
		if (userType.equals("staffs") && keyword == null) {
			return userService.listStaffsByPage(currentPage);
		}
		else if (userType.equals("staffs") && keyword != null) {
			return userService.listStaffsWithKeywordByPage(currentPage, keyword);
		}
		else if (userType.equals("subscribers") && keyword == null) {
			return userService.listSubScribersByPage(currentPage);
		}
		else {
			return userService.listSubScribersWithKeywordByPage(currentPage, keyword);
		}
	}
}
