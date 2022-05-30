package com.lessayer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.lessayer.entity.Article;
import com.lessayer.entity.Role;
import com.lessayer.entity.User;
import com.lessayer.service.ArticleService;

@Controller
public class ArticleController {
	
	@Autowired
	private ArticleService articleService;
	
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
		
		Article article = new Article();
		
		if (article.getId() == null) {
			pageTitle = "Create New Article";
		}
		else {
			pageTitle = "Edit Article ID " + article.getId();
		}
		
		model.addAttribute("article", article);
		model.addAttribute("pageTitle", pageTitle);
		model.addAttribute("returnPage", 0);
		model.addAttribute("baseURL", "/article/articles");
		return "/article/article_form";
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
