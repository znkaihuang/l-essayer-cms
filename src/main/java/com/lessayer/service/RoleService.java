package com.lessayer.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lessayer.entity.Role;
import com.lessayer.repository.RoleRepository;

@Service
public class RoleService {
	
	@Autowired
	private RoleRepository roleRepo;
	
	public List<Role> findStaffs() {
		List<Role> staffList = ((List<Role>) roleRepo.findAll()).stream()
								.filter(role -> role.getName().equals("Subscribers") == false)
								.collect(Collectors.toList());

		return staffList;
	}
	
	public List<Role> findRoles() {
		return (List<Role>) roleRepo.findAll();
	}
	
}
