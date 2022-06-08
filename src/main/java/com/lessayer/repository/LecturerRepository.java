package com.lessayer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.lessayer.entity.Lecturer;

public interface LecturerRepository extends PagingAndSortingRepository<Lecturer, Integer> {
	
	@Query("SELECT l FROM Lecturer l")
	public List<Lecturer> findAllLecturers();
	
}
