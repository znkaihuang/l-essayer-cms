package com.lessayer.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.lessayer.entity.Tag;

public interface TagRepository extends CrudRepository<Tag, Integer> {
	
	@Modifying
	@Query("UPDATE Tag t SET t.tag = ?2 WHERE t.id = ?1")
	public void updateTag(Integer id, String tag);
	
}
