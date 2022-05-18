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
import org.springframework.web.bind.annotation.ResponseBody;
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
	
	@GetMapping("/user/staffs")
	public String showStaffPage(Model model, String keyword) {
		return showStaffPageByPage(0, model, keyword);
	}
	
	@GetMapping("/user/staffs/{pageNum}")
	public String showStaffPageByPage(@PathVariable("pageNum") Integer currentPage, Model model,
		String keyword) {
		Page<User> page;
		
		if (keyword == null) {
			page = userService.listStaffsByPage(currentPage);
		}
		else {
			page = userService.listStaffsWithKeywordByPage(currentPage, keyword);
		}
		
		Integer totalPages = page.getTotalPages();
		Integer prevPage = (currentPage - 1 >= 0) ? currentPage - 1 : 0;
		Integer nextPage = (currentPage + 1 < totalPages) ? currentPage + 1 : totalPages - 1;
		
		model.addAttribute("staffList", page.getContent());
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("prevPage", prevPage);
		model.addAttribute("nextPage", nextPage);
		model.addAttribute("keyword", keyword);
		return "/user/users";
	}
	
	@GetMapping("/user/staffs/createStaff")
	public String showCreateStaffPage(Model model) {
		List<Role> roleList = roleService.findStaffs();
		User user = new User();
		user.setPhotos("/images/user-solid.svg");
		
		model.addAttribute("roleList", roleList);
		model.addAttribute("user", user);
		model.addAttribute("pageTitle", "Create New Staff");
		model.addAttribute("returnPage", 0);
		return "/user/user_form";
	}
	
	@PostMapping("/user/staffs/save/{pageNum}")
	public String saveStaff(@PathVariable("pageNum") Integer pageNum,
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
		
		return formatRedirectURL("redirect:/user/staffs/" + pageNum, keyword);
	}

	@ResponseBody
	@GetMapping("/user/staffs/createStaff/checkEmailUnique")
	public Boolean checkEmailUnique() {
		return true;
	}
	
	@GetMapping("/user/staffs/editStaff/{pageNum}/{userId}/{showId}")
	public String editStaff(@PathVariable("pageNum") Integer pageNum, 
		@PathVariable("userId") Integer userId, Model model, RedirectAttributes redirectAttributes,
		String keyword) {
		Optional<User> userOptional = userService.findUserById(userId);
		
		if (userOptional.isEmpty()) {
			setModalMessage(redirectAttributes, "Error", "Cannot find User with ID " + userId);
			return "redirect:/user/staffs";
		}
		else {
			List<Role> roleList = roleService.findStaffs();
			
			model.addAttribute("user", userOptional.get());
			model.addAttribute("roleList", roleList);
			model.addAttribute("pageTitle", "Edit Staff ID " + userId);
			model.addAttribute("returnPage", pageNum);
			if (keyword != null) {
				model.addAttribute("keyword", keyword);
			}
			
			return "/user/user_form";
		}
	}
	
	@GetMapping("/user/staffs/viewStaff/{pageNum}/{userId}/{showId}")
	public String viewStaff(@PathVariable("pageNum") Integer pageNum,
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
		
		return formatRedirectURL("redirect:/user/staffs/" + pageNum, keyword);
	}
	
	@GetMapping("/user/staffs/requestRemoveStaff/{pageNum}/{userId}/{showId}")
	public String requestRemoveStaff(@PathVariable("pageNum") Integer pageNum,
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
		}
		
		return formatRedirectURL("redirect:/user/staffs/" + pageNum, keyword);
	}
	
	@GetMapping("/user/staffs/removeStaff/{pageNum}/{userId}/{showId}")
	public String removeStaff(@PathVariable("pageNum") Integer pageNum,
			@PathVariable("userId") Integer userId, @PathVariable("showId") Integer showId,
			RedirectAttributes redirectAttributes, String keyword) {
		
		userService.deleteUserById(userId);
		FileUploadUtil.remove("user-photos/" + userId);
		
		if (showId == 0 && pageNum > 0) {
			pageNum--;
		}
		setModalMessage(redirectAttributes, "Success", "Successfully delete user with ID " + userId);
		
		return formatRedirectURL("redirect:/user/staffs/" + pageNum, keyword);
	}
	
	@GetMapping("/user/staffs/exportCsv")
	public void exportToCsv(HttpServletResponse response) throws IOException {
		List<User> staffList = userService.listAllStaffs();
		AbstractFileExporter<User> csvExporter = UserCsvExporterDelegate.getCsvExporter();
		csvExporter.export(staffList, response);
	}
	
	@GetMapping("/user/staffs/exportPdf")
	public void exportToPdf(HttpServletResponse response) throws IOException {
		List<User> staffList = userService.listAllStaffs();
		AbstractFileExporter<User> pdfExporter = UserPdfExporterDelegate.getPdfExporter();
		pdfExporter.export(staffList, response);
	}
	
	private void uploadPhoto(Integer userId, String fileName, MultipartFile imageFile)
		throws IOException {
		String uploadDir = "user-photos/" + userId;
		FileUploadUtil.cleanDir(uploadDir);
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
}
