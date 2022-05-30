package com.lessayer.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.lessayer.entity.Article;

public interface ArticleRepository extends PagingAndSortingRepository<Article, Integer> {
	
	@Query("SELECT a FROM Article a WHERE a.author = ?1")
	public List<Article> findArticlesWithAuthor(String author);
	
	@Query("SELECT a FROM Article a WHERE a.author = ?1")
	public Page<Article> findArticlesWithAuthor(String author, Pageable page);
	
	@Query("SELECT a FROM Article a WHERE a.date < ?1")
	public List<Article> findArticlesBeforeDate(Date date);
	
	@Query("SELECT a FROM Article a WHERE a.date < ?1")
	public Page<Article> findArticlesBeforeDate(Date date, Pageable page);
	
	@Query("SELECT a FROM Article a WHERE a.date = ?1")
	public List<Article> findArticlesEqualDate(Date date);
	
	@Query("SELECT a FROM Article a WHERE a.date = ?1")
	public Page<Article> findArticlesEqualDate(Date date, Pageable page);
	
	@Query("SELECT a FROM Article a WHERE a.date > ?1")
	public List<Article> findArticlesAfterDate(Date date);
	
	@Query("SELECT a FROM Article a WHERE a.date > ?1")
	public Page<Article> findArticlesAfterDate(Date date, Pageable page);
	
	@Query("SELECT a FROM Article a WHERE a.date >= ?1 AND a.date <= ?2")
	public List<Article> findArticlesBetweenDates(Date beginDate, Date endDate);
	
	@Query("SELECT a FROM Article a WHERE a.date >= ?1 AND a.date <= ?2")
	public Page<Article> findArticlesBetweenDates(Date beginDate, Date endDate, Pageable page);
	
	@Query("SELECT DISTINCT a FROM Article a JOIN a.tags t WHERE t.tag = ?1")
	public List<Article> findArticlesWithTag(String tag);
	
	@Query("SELECT DISTINCT a FROM Article a JOIN a.tags t WHERE t.tag = ?1")
	public Page<Article> findArticlesWithTag(String tag, Pageable page);
	
	@Query("SELECT DISTINCT a FROM Article a JOIN a.tags t WHERE CONCAT(a.title, ' ', a.author, ' ', a.summary, ' ', t.tag) Like %?1%")
	public List<Article> findArticlesWithKeyword(String keyword);
	
	@Query("SELECT DISTINCT a FROM Article a JOIN a.tags t WHERE CONCAT(a.title, ' ', a.author, ' ', a.summary, ' ', t.tag) Like %?1%")
	public Page<Article> findArticlesWithKeyword(String keyword, Pageable page);
}
