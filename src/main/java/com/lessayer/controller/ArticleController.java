package com.lessayer.controller;


import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lessayer.entity.Article;
import com.lessayer.entity.Tag;
import com.lessayer.service.ArticleService;
import com.lessayer.service.TagService;

@Controller
public class ArticleController {
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private TagService tagService;
	
	@GetMapping("/article/articles")
	public String showArticlePage(Model model, String keyword) {
		return showArticlePageByPage(0, model, keyword);
	}
	
	@GetMapping("/article/articles/{pageNum}")
	public String showArticlePageByPage(@PathVariable("pageNum") Integer currentPage, Model model,
		String keyword) {
		Page<Article> page;
		
		page = listArticlePage(currentPage, keyword);
		
		Integer totalPages = page.getTotalPages();
		Integer prevPage = (currentPage - 1 >= 0) ? currentPage - 1 : 0;
		Integer nextPage = (currentPage + 1 < totalPages) ? currentPage + 1 : totalPages - 1;
		
		model.addAttribute("articleList", page.getContent());
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("prevPage", prevPage);
		model.addAttribute("nextPage", nextPage);
		model.addAttribute("keyword", keyword);
		model.addAttribute("baseURL", "/article/articles");
		
		return "/article/articles";
	}
	
	@GetMapping("/article/articles/create")
	public String showCreateArticlePage(Model model) {
		String pageTitle;
		List<Tag> tagList = tagService.retrieveAllTags();
		
		Article article = new Article();
		
		if (article.getId() == null) {
			pageTitle = "Create New Article";
		}
		else {
			pageTitle = "Edit Article ID " + article.getId();
		}
		
		model.addAttribute("article", article);
		model.addAttribute("tagList", tagList);
		model.addAttribute("pageTitle", pageTitle);
		model.addAttribute("returnPage", 0);
		model.addAttribute("baseURL", "/article/articles");
		return "/article/article_form";
	}
	
	@PostMapping("/article/articles/save/{pageNum}")
	public String saveArticle(@PathVariable("pageNum") Integer pageNum, Article article, 
		RedirectAttributes redirectAttributes, String keyword, MultipartFile imageFile1,
		MultipartFile imageFile2, MultipartFile imageFile3) throws IOException {
		
		System.out.println(article.getContent());
		
		return formatRedirectURL("redirect:/article/articles/" + pageNum, keyword);
	}
	
	private String formatRedirectURL(String redirectURL, String keyword) {
		return (keyword == null) ? redirectURL : redirectURL + "?keyword=" + keyword;
	}
	
	private Page<Article> listArticlePage(Integer currentPage, String keyword) {
		if (keyword == null) {
			return articleService.listArticleByPage(currentPage);
		}
		else {
			return articleService.listArticleWithKeywordByPage(currentPage, keyword);
		}
	}
	
}
