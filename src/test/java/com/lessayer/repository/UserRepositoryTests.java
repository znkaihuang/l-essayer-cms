package com.lessayer.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import com.lessayer.entity.Role;
import com.lessayer.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private EntityManager entityManager;
	
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Test
	public void createUserWithOneRoleTest() throws ParseException {
		String email = "ohmygod8269@gmail.com";
		String password = passwordEncoder.encode("ohmygod8269");
		String firstName = "Zhen Kai";
		String lastName = "Huang";
		User adminUser = new User(email, password, firstName, lastName);
		Role adminRole = entityManager.find(Role.class, 1);
		adminUser.addRole(adminRole);
		
		User savedUser = userRepo.save(adminUser);
		
		assertThat(savedUser).isNotNull();
	}
	
	@Test
	public void createUserWithMultiRolesTst() {
		String email = "pomin@mail.com";
		String password = passwordEncoder.encode("pomin123");
		String firstName = "Po Min";
		String lastName = "Shi";
		User user = new User(email, password, firstName, lastName);
		Role editorRole = entityManager.find(Role.class, 2);
		Role salesPersonRole = entityManager.find(Role.class, 3);
		Role assistantRole = entityManager.find(Role.class, 4);
		user.addRole(editorRole);
		user.addRole(salesPersonRole);
		user.addRole(assistantRole);
		
		User savedUser = userRepo.save(user);
		
		assertThat(savedUser).isNotNull();
	}
	
	@Test
	public void findUserByEmailTest() {
		String email = "ohmygod8269@gmail.com";
		
		Optional<User> userOptional = userRepo.findUserByEmail(email);
		User userInDB = userOptional.get();
		
		assertThat(userInDB).isNotNull();
		assertThat(userInDB.getEmail()).isEqualTo(email);
	}
	
	@Test
	public void findAllUsersTest() {
		int pageNumber = 0;
		int pageSize = 5;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = userRepo.findAllUsers(pageable);
		List<User> userList = page.getContent();
		
		userList.forEach(System.out::println);
		
		assertThat(userList.size()).isGreaterThan(0);
	}
	
	@Test
	public void findUsersWithKeywordTest() {
		int pageNumber = 0;
		int pageSize = 5;
		String keyword = "gmail";
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = userRepo.findStaffsWithKeyword(keyword, pageable);
		List<User> userList = page.getContent();
		
		userList.forEach(System.out::println);
		
		assertThat(userList.size()).isGreaterThan(0);
	}
	
	@Test
	public void updateUserEmailTest() {
		int userId = 2;
		String newEmail = "pomin-shi@mail.com";
		
		User userInDB = userRepo.findById(userId).get();
		
		userInDB.setEmail(newEmail);
		
		User updatedUser = userRepo.save(userInDB);
		
		assertThat(updatedUser.getEmail()).isEqualTo(newEmail);
	}
	
	@Test
	public void updateUserRoleTest() {
		int userId = 2;
		Role customerRole = entityManager.find(Role.class, 5);
		
		User userInDB1 = userRepo.findById(userId).get();
		userInDB1.addRole(customerRole);
		User updatedUser1 = userRepo.save(userInDB1);
		
		assertThat(updatedUser1.hasRole("Customer")).isTrue();
		
		User userInDB2 = userRepo.findById(userId).get();
		userInDB2.removeRole(customerRole);
		User updatedUser2 = userRepo.save(userInDB2);
		
		assertThat(updatedUser2.hasRole("Customer")).isFalse();
	}
	
	@Test
	public void updateUserNameTest() {
		int userId = 2;
		String newFirstName = "Po-Min";
		User userInDB = userRepo.findById(userId).get();
		
		userInDB.setFirstName(newFirstName);
		
		User updatedUser = userRepo.save(userInDB);
		
		assertThat(updatedUser.getFirstName()).isEqualTo(newFirstName);
	}
	
	@Test
	public void enableUserTest() {
		int userId = 1;
		User userInDB = userRepo.findById(userId).get();
		
		userInDB.setEnabled(true);
		
		User updatedUser = userRepo.save(userInDB);
		
		assertThat(updatedUser.isEnabled()).isTrue();
	}
	
	@Test
	public void disableUserTest() {
		int userId = 2;
		User userInDB = userRepo.findById(userId).get();
		
		userInDB.setEnabled(false);
		
		User updatedUser = userRepo.save(userInDB);
		
		assertThat(updatedUser.isEnabled()).isFalse();
	}
	
}
