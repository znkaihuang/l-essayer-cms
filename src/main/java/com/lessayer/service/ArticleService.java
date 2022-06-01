package com.lessayer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lessayer.entity.Article;
import com.lessayer.repository.ArticleRepository;

@Service
public class ArticleService {
	
	public static Integer ARTICLES_PER_PAGE = 6;
	
	@Autowired
	private ArticleRepository articleRepository;
	
	public Page<Article> listArticleByPage(Integer currentPage) {
		Pageable pageable = PageRequest.of(currentPage, ARTICLES_PER_PAGE);
		Page<Article> page = articleRepository.findAll(pageable);
		
		return page;
	}

	public Page<Article> listArticleWithKeywordByPage(Integer currentPage, String keyword) {
		Pageable pageable = PageRequest.of(currentPage, ARTICLES_PER_PAGE);
		Page<Article> page = articleRepository.findArticlesWithKeyword(keyword, pageable);
		
		return page;
	}

	public Article saveArticle(Article article) {
		return articleRepository.save(article);
	}

}
