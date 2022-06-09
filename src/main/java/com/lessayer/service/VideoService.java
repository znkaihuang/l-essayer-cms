package com.lessayer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lessayer.entity.Video;
import com.lessayer.repository.VideoRepository;

@Service
public class VideoService {
	
	public static Integer VIDEOS_PER_PAGE = 6;
	
	@Autowired
	private VideoRepository videoRepository;
	
	public List<Video> listAllVideos() {
		return videoRepository.findAllVideos();
	}
	
	public Page<Video> listVideoByPage(Integer currentPage) {
		Pageable pageable = PageRequest.of(currentPage, VIDEOS_PER_PAGE);
		Page<Video> page = videoRepository.findAll(pageable);
		
		return page;
	}

	public Page<Video> listVideoWithKeywordByPage(Integer currentPage, String keyword) {
		Pageable pageable = PageRequest.of(currentPage, VIDEOS_PER_PAGE);
		Page<Video> page = videoRepository.findVideosWithKeyword(keyword, pageable);
		
		return page;
	}

}
