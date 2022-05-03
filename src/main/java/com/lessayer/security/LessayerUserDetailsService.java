package com.lessayer.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.lessayer.entity.User;
import com.lessayer.repository.UserRepository;

public class LessayerUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> userOptional = userRepo.findUserByEmail(email);
		
		if (userOptional.isEmpty()) {
			throw new UsernameNotFoundException("Cannot find user with email: " + email + " .");
		}
		
		return new LessayerUserDetails(userOptional.get());
	}

}
