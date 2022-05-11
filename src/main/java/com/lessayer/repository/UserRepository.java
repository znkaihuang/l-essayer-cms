package com.lessayer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.lessayer.entity.User;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
	
	@Query("SELECT DISTINCT u FROM User u JOIN u.roles r WHERE r.name != 'Subscribers'")
	public List<User> findAllStaffs();
	
	@Query("SELECT DISTINCT u FROM User u JOIN u.roles r WHERE r.name = 'Subscribers'")
	public List<User> findAllSubscribers();
	
	@Query("SELECT DISTINCT u FROM User u JOIN u.roles r WHERE r.name != 'Subscribers'")
	public Page<User> findAlStaffs(Pageable pageable);
	
	@Query("SELECT DISTINCT u FROM User u JOIN u.roles r WHERE r.name = 'Subscribers'")
	public Page<User> findAllSubscribers(Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.email = ?1")
	public Optional<User> findUserByEmail(String email);
	
	@Query("SELECT u FROM User u")
	public Page<User> findAllUsers(Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ', u.firstName, ' ', u.lastName) "
			+ "LIKE %?1%")
	public Page<User> findUsersWithKeyword(String keyword, Pageable pageable);
	
	@Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled); 
	
}
