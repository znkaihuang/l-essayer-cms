package com.lessayer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.lessayer.entity.Video;

public interface VideoRepository extends PagingAndSortingRepository<Video, Integer> {
	
	@Query("SELECT v FROM Video v")
	public List<Video> findAllVideos();
	
}
