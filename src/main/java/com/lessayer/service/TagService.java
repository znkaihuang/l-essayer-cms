package com.lessayer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lessayer.entity.Tag;
import com.lessayer.repository.TagRepository;

@Service
public class TagService {
	
	@Autowired
	private TagRepository tagRepository;
	
	public List<Tag> retrieveAllTags() {
		return tagRepository.findAllTags();
	}

}
