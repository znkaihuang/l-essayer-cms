package com.lessayer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lessayer.entity.Language;
import com.lessayer.entity.Video;
import com.lessayer.filter.FilterQueryObject;
import com.lessayer.repository.VideoRepository;

@Service
public class VideoService {
	
	public static Integer VIDEOS_PER_PAGE = 6;
	
	@Autowired
	private VideoRepository videoRepository;
	
	public VideoService(VideoRepository videoRepository) {
		this.videoRepository = videoRepository;
	}

	public List<Video> listAllVideos() {
		return videoRepository.findAllVideos();
	}
	
	public Page<Video> listVideosByPage(Integer currentPage) {
		Pageable pageable = PageRequest.of(currentPage, VIDEOS_PER_PAGE);
		Page<Video> page = videoRepository.findAll(pageable);
		
		return page;
	}

	public Page<Video> listVideosWithKeywordByPage(Integer currentPage, String keyword) {
		Pageable pageable = PageRequest.of(currentPage, VIDEOS_PER_PAGE);
		Page<Video> page = videoRepository.findVideosWithKeyword(keyword, pageable);
		
		return page;
	}
	
	public Page<Video> listVideosWithFilter(Integer currentPage, List<FilterQueryObject> filterQueryList) {
		Pageable pageable = PageRequest.of(currentPage, VIDEOS_PER_PAGE);
		
		List<List<Video>> videoListArray = new ArrayList<>();
		for (FilterQueryObject filterQuery : filterQueryList) {
			List<Video> videoList = new ArrayList<>();
			for (String query : filterQuery.getQueryOptions()) {
				if (filterQuery.getSectionName().equals("Language")) {
					switch (query) {
						case "English":
							videoList.addAll(videoRepository.findVideosWithLanguage(Language.EN));
							break;
						case "French":
							videoList.addAll(videoRepository.findVideosWithLanguage(Language.FR));
							break;
						case "Chinese":
							videoList.addAll(videoRepository.findVideosWithLanguage(Language.ZH_TW));
							break;
					}
				}
				else if (filterQuery.getSectionName().equals("VideoLength")) {
					switch (query) {
						case "0-10minutes":
							videoList.addAll(videoRepository.findVideosBetweenLength(0 * 60, 10 * 60));
							break;
						case "11-30minutes":
							videoList.addAll(videoRepository.findVideosBetweenLength(11 * 60, 30 * 60));
							break;
						case "31-60minutes":
							videoList.addAll(videoRepository.findVideosBetweenLength(31 * 60, 60 * 60));
							break;
						case "Morethan1hour":
							videoList.addAll(videoRepository.findVideosLargerThanLength(61 * 60));
							break;
					}
				}
				else {
					switch (query) {
					case "Withsubtitle":
						videoList.addAll(videoRepository.findVideosBySubtitle(true));
						break;
					case "Withoutsubtitle":
						videoList.addAll(videoRepository.findVideosBySubtitle(false));
						break;
					}
				}
			}
			videoListArray.add(videoList);
		}
		
		List<Video> finalContent = videoListArray.get(0);
		if (videoListArray.size() > 1) {
			for (int index = 1; index < videoListArray.size(); index++) {
				List<Video> filterList = videoListArray.get(index);
				finalContent = finalContent.stream().filter(video -> filterList.contains(video)).toList();
			}
		}
		
		Integer lastIndex = (finalContent.size() >= VIDEOS_PER_PAGE) ? VIDEOS_PER_PAGE : finalContent.size();
		Page<Video> returnPage = new PageImpl<Video>(finalContent.subList(0, lastIndex), pageable, finalContent.size());
		
		return returnPage;
	}
}
