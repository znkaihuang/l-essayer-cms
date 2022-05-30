package com.lessayer.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class ArticleTests {
	
	@Test
	public void articleEntityTest() {
		Set<Tag> tags = new HashSet<>();
		tags.add(new Tag("Tag 1"));
		tags.add(new Tag("Tag 2"));
		Article article = new Article("Title", "Author", new Date(), "Summary", "Content", tags);
		
		System.out.println(article);
		System.out.println(article.getDateString());
	}
	
}
