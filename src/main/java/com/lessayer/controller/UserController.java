package com.lessayer.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

import com.lessayer.FileUploadUtil;
import com.lessayer.entity.Role;
import com.lessayer.entity.User;
import com.lessayer.service.RoleService;
import com.lessayer.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@GetMapping("/user/staffs")
	public String showStaffPage(Model model) {
		return showStaffPageByPage(0, model);
	}
	
	public String showStaffPageByPage(Integer pageNum, Model model) {
		Page<User> page = userService.listStaffsByPage(0);
		model.addAttribute("staffList", page.getContent());
		return "users";
	}
	
	@GetMapping("/user/staffs/createStaff")
	public String showCreateStaffPage(Model model) {
		List<Role> roleList = roleService.findStaffs();
		User user = new User();
		user.setPhotos("/images/user-solid.svg");
		
		model.addAttribute("roleList", roleList);
		model.addAttribute("user", user);
		model.addAttribute("pageTitle", "Create New Staff");
		return "user_form";
	}
	
	@PostMapping("/user/staffs/save")
	public String saveStaff(User user, String enabled, String disabled,
			RedirectAttributes redirectAttributes,
			@RequestParam("imageFile") MultipartFile imageFile) throws IOException {
		// Create new staff
		if (user.getId() == null) {
			if (enabled.equals("true")) {
				user.setEnabled(true);
			}
			else {
				user.setEnabled(false);
			}
			String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = userService.saveUser(user);
			uploadPhoto(savedUser.getId(), fileName, imageFile);
			redirectAttributes.addFlashAttribute("modalTitle", "Success");
			redirectAttributes.addFlashAttribute("modalBody", "Create User with ID " + savedUser.getId());
		}
		// Update existed staff
		else {
			if (!imageFile.isEmpty()) {
				String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
				user.setPhotos(fileName);
				uploadPhoto(user.getId(), fileName, imageFile);
			}
			userService.saveUser(user);
			redirectAttributes.addFlashAttribute("modalTitle", "Success");
			redirectAttributes.addFlashAttribute("modalBody", "Update User with ID " + user.getId());
		}
		return "redirect:/user/staffs";
	}
	
	@ResponseBody
	@GetMapping("/user/staffs/createStaff/checkEmailUnique")
	public Boolean checkEmailUnique() {
		return true;
	}
	
	@GetMapping("/user/staffs/editStaff/{userId}")
	public String editStaff(@PathVariable("userId") Integer userId, Model model,
		RedirectAttributes redirectAttributes) {
		Optional<User> userOptional = userService.findUserById(userId);
		
		if (userOptional.isEmpty()) {
			redirectAttributes.addFlashAttribute("modalTitle", "Error");
			redirectAttributes.addFlashAttribute("modalBody", "Cannot find User with ID " + userId);
			return "redirect:/user/staffs";
		}
		else {
			List<Role> roleList = roleService.findStaffs();
			
			model.addAttribute("user", userOptional.get());
			model.addAttribute("roleList", roleList);
			model.addAttribute("pageTitle", "Edit Staff ID " + userId);
		}
		
		return "user_form";
	}
	
	@GetMapping("/user/staffs/viewStaff/{userId}/{showId}")
	public String viewStaff(@PathVariable("userId") Integer userId,
		@PathVariable("showId") Integer showId,
		RedirectAttributes redirectAttributes, Model model) {
		Optional<User> userOptional = userService.findUserById(userId);
		
		if (userOptional.isEmpty()) {
			redirectAttributes.addFlashAttribute("modalTitle", "Error");
			redirectAttributes.addFlashAttribute("modalBody", "Cannot find User with ID " + userId);
		}
		else {
			Page<User> page = userService.listStaffsByPage(0);
			redirectAttributes.addFlashAttribute("modalTitle", "User " + userOptional.get().getId());
			redirectAttributes.addFlashAttribute("showId", showId);
		}
		return "redirect:/user/staffs";
	}
	
	private void uploadPhoto(Integer userId, String fileName, MultipartFile imageFile)
		throws IOException {
		String uploadDir = "user-photos/" + userId;
		FileUploadUtil.cleanDir(uploadDir);
		FileUploadUtil.saveFile(uploadDir, fileName, imageFile);
	}
	
}
