package com.lessayer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lessayer.entity.Tag;
import com.lessayer.repository.TagRepository;

@Service
public class TagService {
	
	@Autowired
	private TagRepository tagRepository;
	
	public static List<Tag> TAG_LIST_CACHE;
	
	public List<Tag> retrieveAllTags() {
		if (TAG_LIST_CACHE == null) {
			TAG_LIST_CACHE = new ArrayList<>();
			TAG_LIST_CACHE = tagRepository.findAllTags();
		}
		
		return TAG_LIST_CACHE;
	}

	public Tag createTag(Tag tag) {
		return tagRepository.save(tag);
	}

}
