package com.lessayer.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.lessayer.entity.Video;

public interface VideoRepository extends PagingAndSortingRepository<Video, Integer> {
	
	@Query("SELECT v FROM Video v")
	public List<Video> findAllVideos();
	
	@Query("SELECT DISTINCT v FROM Video v JOIN v.lecturer l WHERE CONCAT(v.title, ' ', v.uploader, ' ',"
			+ " l.firstName, ' ', l.lastName, ' ', v.date, ' ', v.description, ' ', v.url) Like %?1%")
	public Page<Video> findVideosWithKeyword(String keyword, Pageable pageable);
	
}
