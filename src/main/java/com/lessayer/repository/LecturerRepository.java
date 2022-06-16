package com.lessayer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.lessayer.entity.Lecturer;

public interface LecturerRepository extends PagingAndSortingRepository<Lecturer, Integer> {
	
	@Query("SELECT l FROM Lecturer l")
	public List<Lecturer> findAllLecturers();

	@Query("SELECT l FROM Lecturer l WHERE l.name = ?1")
	public Optional<Lecturer> findByName(String name);
	
}
