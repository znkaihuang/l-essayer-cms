package com.lessayer.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
	
	public String showUserByPage() {
		return "user";
	}
	
	@GetMapping("/user/staffs/createStaff")
	public String showCreateStaffPage(Model model) {
		List<Role> roleList = roleService.findStaffs();
		User user = new User();
		user.setPhotos("/images/user-solid.svg");
		
		model.addAttribute("roleList", roleList);
		model.addAttribute("user", user);
		return "user_form";
	}
	
	@PostMapping("/user/staffs/createStaff")
	public String createStaff(User user, String enabled, String disabled,
			@RequestParam("imageFile") MultipartFile imageFile) throws IOException {
		if (enabled.equals("true")) {
			user.setEnabled(true);
		}
		else {
			user.setEnabled(false);
		}
		String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
		user.setPhotos(fileName);
		User savedUser = userService.createUser(user);
		String uploadDir = "user-photos/" + savedUser.getId();
		
		
		FileUploadUtil.cleanDir(uploadDir);
		FileUploadUtil.saveFile(uploadDir, fileName, imageFile);
		
		return "redirect:/user/staffs";
	}
	
	@ResponseBody
	@GetMapping("/user/staffs/createStaff/checkEmailUnique")
	public Boolean checkEmailUnique() {
		return true;
	}
}
