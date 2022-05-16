package com.lessayer.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lessayer.entity.User;
import com.lessayer.repository.UserRepository;

@Service
@Transactional
public class UserService {
	
	public static Integer USERS_PER_PAGE = 5;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<User> listAllStaffs() {
		return userRepo.findAllStaffs();
	}
	
	public Page<User> listStaffsByPage(Integer pageNum) {
		Pageable pageable = PageRequest.of(pageNum, USERS_PER_PAGE);
		Page<User> page = userRepo.findAll(pageable);
		
		return page;
	}
	
	public boolean checkEmailUnique(String email) {
		return (userRepo.findUserByEmail(email).isEmpty()) ? true : false;
	}
	
	public User saveUser(User user) {
		if (user.getId() == null) {
			user.setPhotos(user.getPhotos().replace(",", ""));
			user.setRegistrationDate(new Date());
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		else {
			// Set the original password back
			user.setPassword(passwordEncoder.encode(userRepo.findById(user.getId()).get().getPassword()));
		}
		System.out.println(user);
		return userRepo.save(user);
	}

	public Optional<User> findUserByEmail(String email) {
		return userRepo.findUserByEmail(email);
	}

	public Optional<User> findUserById(Integer staffId) {
		return userRepo.findById(staffId);
	}
	
}
