package com.lessayer.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.lessayer.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Test
	public void createRoleTest() {
		String roleName = "Adminstrator";
		String description = "Administer the whole backend system.";
		Role savedRole = roleRepo.save(new Role(roleName, description));
		
		assertThat(savedRole).isNotNull();
	}
	
	@Test
	public void createMultiRolesTest() {
		Role savedRole1 = roleRepo.save(new Role("Editor", "Manage the articles, dictionnaire system, and the quiz system."));
		Role savedRole2 = roleRepo.save(new Role("Salesperson", "Manage the subscription of the customers and the promotion of this system."));
		Role savedRole3 = roleRepo.save(new Role("Assistant", "Reply customer's questions and review the comments."));
		Role savedRole4 = roleRepo.save(new Role("Customer", "User of this system."));
		
		assertThat(savedRole1).isNotNull();
		assertThat(savedRole2).isNotNull();
		assertThat(savedRole3).isNotNull();
		assertThat(savedRole4).isNotNull();
	}
	
}
