package com.lessayer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lessayer.converter.ContentConverter;
import com.lessayer.converter.ContentConverter.ConvertType;
import com.lessayer.entity.Article;
import com.lessayer.entity.ArticleImage;
import com.lessayer.repository.ArticleImageRepository;
import com.lessayer.repository.ArticleRepository;

@Service
public class ArticleService {
	
	public static Integer ARTICLES_PER_PAGE = 6;
	private final String imageFilePath = "http://localhost:8080/article-image-files/";
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private ArticleImageRepository articleImageRepository;
	
	public List<Article> listAllArticles() {
		return articleRepository.findAllArticles();
	}
	
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
	
	public Optional<Article> findArticleById(Integer articleId) {
		return articleRepository.findById(articleId);
	}
	
	public Article saveArticle(Article article) {
		return articleRepository.save(article);
	}

	public void setImageFiles(Article article, MultipartFile[] imageFiles) {
		if (article.getImageFiles() == null) {
			article.setImageFiles(new ArrayList<ArticleImage>());
		}
		article.removeAllImageFiles();
		
		for (MultipartFile imageFile : imageFiles) {
			if (!imageFile.isEmpty()) {
				ArticleImage savedImage = articleImageRepository.save(new ArticleImage(imageFile.getOriginalFilename(), article));
				article.addImageFiles(savedImage);
			}
		}
	}

	public String createArticleContentHTML(Article article) {
		ContentConverter markdownHTMLConverter = ContentConverter.getInstance(ConvertType.Markdown, ConvertType.HTML);
		for (ArticleImage image : article.getImageFiles()) {
			String imageFileName = image.getFileName();
			article.setContent(article.getContent().replaceAll(imageFileName, imageFilePath + article.getId() + "/" + imageFileName));
		}
		return markdownHTMLConverter.convertContentToHTMLString(article.getContent());
	}

	public void deleteArticleById(Integer articleId) {
		Optional<Article> articleOptional = articleRepository.findById(articleId);
		
		if (articleOptional.isPresent()) {
			articleRepository.delete(articleOptional.get());
		}
		
	}

}
