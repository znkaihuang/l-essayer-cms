package com.lessayer.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.lessayer.entity.Language;
import com.lessayer.entity.Video;

public interface VideoRepository extends PagingAndSortingRepository<Video, Integer> {
	
	@Query("SELECT v FROM Video v")
	public List<Video> findAllVideos();
	
	@Query("SELECT DISTINCT v FROM Video v JOIN v.lecturer l WHERE CONCAT(v.title, ' ', v.uploader, ' ', v.date, ' ',"
			+ " l.name, ' ', v.date, ' ', v.description, ' ', v.url) Like %?1%")
	public Page<Video> findVideosWithKeyword(String keyword, Pageable pageable);
	
	@Query("SELECT v FROM Video v WHERE v.language = ?1")
	public List<Video> findVideosWithLanguage(Language language);
	
	@Query("SELECT DISTINCT v FROM Video v JOIN v.lecturer l WHERE CONCAT(v.title, ' ', v.uploader, ' ', v.date, ' ',"
			+ " l.name, ' ', v.date, ' ', v.description, ' ', v.url) Like %?1% AND v.language = ?2")
	public List<Video> findVideosWithKeywordAndLanguage(String keyword, Language language);
	
	@Query("SELECT v FROM Video v WHERE v.hasSubtitle = ?1")
	public List<Video> findVideosBySubtitle(boolean hasSubtitle);
	
	@Query("SELECT DISTINCT v FROM Video v JOIN v.lecturer l WHERE CONCAT(v.title, ' ', v.uploader, ' ', v.date, ' ',"
			+ " l.name, ' ', v.date, ' ', v.description, ' ', v.url) Like %?1% AND v.hasSubtitle = ?2")
	public List<Video> findVideosWithKeywordAndSubtitle(String keyword, boolean hasSubtitle);
	
	@Query("SELECT v FROM Video v WHERE v.videoLength >= ?1 AND v.videoLength <= ?2")
	public List<Video> findVideosBetweenLength(Integer lowerBound, Integer upperBound);
	
	@Query("SELECT DISTINCT v FROM Video v JOIN v.lecturer l WHERE CONCAT(v.title, ' ', v.uploader, ' ', v.date, ' ',"
			+ " l.name, ' ', v.date, ' ', v.description, ' ', v.url) Like %?1% AND"
			+ " v.videoLength >= ?2 AND v.videoLength <= ?3")
	public List<Video> findVideosWithKeywordAndBetweenLength(String keyword, Integer lowerBound, Integer upperBound);
	
	@Query("SELECT v FROM Video v WHERE v.videoLength >= ?1")
	public List<Video> findVideosLargerThanLength(Integer lowerBound);
	
	@Query("SELECT DISTINCT v FROM Video v JOIN v.lecturer l WHERE CONCAT(v.title, ' ', v.uploader, ' ', v.date, ' ',"
			+ " l.name, ' ', v.date, ' ', v.description, ' ', v.url) Like %?1% AND"
			+ " v.videoLength >= ?2")
	public List<Video> findVideosWithKeywordAndLargerThanLength(String keyword, Integer lowerBound);
}
