package com.lessayer.controller;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lessayer.AbstractFileExporter;
import com.lessayer.AmazonS3Util;
//import com.lessayer.FileUploadUtil;
import com.lessayer.entity.Article;
import com.lessayer.entity.ArticleImage;
import com.lessayer.entity.Tag;
import com.lessayer.exporter.ArticleCsvExporterDelegate;
import com.lessayer.exporter.ArticlePdfExporterDelegate;
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
		model.addAttribute("suffixURL", returnKeywordSuffixURL(keyword));
		
		return "/article/articles";
	}
	
	@GetMapping("/article/articles/create")
	public String showCreateArticlePage(Model model) {
		String pageTitle;
		List<Tag> tagList = tagService.retrieveAllTags();
		
		Article article = new Article();
		article.setImageFiles(new ArrayList<ArticleImage>());
		
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
		RedirectAttributes redirectAttributes, String keyword, MultipartFile[] imageFile,
		String tag, String date) throws IOException, ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		article.setDate(dateFormat.parse(date));
		updateTags(article, parseTagString(tag));

		Article savedArticle = articleService.saveArticle(article);
		
		if (!imageFile[0].isEmpty()) {
			uploadImageFiles(savedArticle.getId(), imageFile);
			articleService.setImageFiles(savedArticle, imageFile);
		}
		
		String modalBody = (article.getId() == null) ? "Create Article with ID " + savedArticle.getId() : "Update Article with ID " + article.getId();
		setModalMessage(redirectAttributes, "Success", modalBody);
		
		return formatRedirectURL("redirect:/article/articles/" + pageNum, keyword);
	}
	
	@GetMapping("/article/articles/exportCsv")
	public void exportToCsv(HttpServletResponse response) throws IOException {
		List<Article> articleList = articleService.listAllArticles();
		AbstractFileExporter<Article> csvExporter = ArticleCsvExporterDelegate.getCsvExporter();
		csvExporter.export(articleList, response);
	}
	
	@GetMapping("/article/articles/exportPdf")
	public void exportToPdf(HttpServletResponse response) throws IOException {
		List<Article> articleList = articleService.listAllArticles();
		AbstractFileExporter<Article> pdfExporter = ArticlePdfExporterDelegate.getPdfExporter();
		pdfExporter.export(articleList, response);
	}
	
	@GetMapping("/article/articles/view/{pageNum}/{articleId}/{showId}")
	public String viewArticle(@PathVariable("pageNum") Integer pageNum, @PathVariable("articleId") Integer articleId, 
		@PathVariable("showId") Integer showId, RedirectAttributes redirectAttributes, String keyword) throws UnsupportedEncodingException {
		Optional<Article> articleOptional = articleService.findArticleById(articleId);
		
		if (articleOptional.isEmpty()) {
			setModalMessage(redirectAttributes, "Error", "Cannot find Article with ID " + articleId);
		}
		else {
			String contentHTML = articleService.createArticleContentHTML(articleOptional.get());
			setModalMessage(redirectAttributes, "Article " + articleId, contentHTML);
			redirectAttributes.addFlashAttribute("showId", showId);
		}
		
		return formatRedirectURL("redirect:/article/articles/" + pageNum, keyword);
	}
	
	@GetMapping("/article/articles/edit/{pageNum}/{articleId}/{showId}")
	public String editUser(@PathVariable("pageNum") Integer pageNum, @PathVariable("articleId") Integer articleId, 
		Model model, RedirectAttributes redirectAttributes, String keyword) {
		Optional<Article> articleOptional = articleService.findArticleById(articleId);
		
		if (articleOptional.isEmpty()) {
			setModalMessage(redirectAttributes, "Error", "Cannot find Article with ID " + articleId);
			return "redirect:/article/articles";
		}
		else {
			String pageTitle = "Edit Article ID " + articleId;
			List<Tag> tagList = tagService.retrieveAllTags();
			
			model.addAttribute("article", articleOptional.get());
			model.addAttribute("tagList", tagList);
			model.addAttribute("pageTitle", pageTitle);
			model.addAttribute("returnPage", pageNum);
			if (keyword != null) {
				model.addAttribute("keyword", keyword);
			}
			model.addAttribute("baseURL", "/article/articles");
			
			return "/article/article_form";
		}
	}
	
	@GetMapping("/article/articles/requestRemove/{pageNum}/{articleId}/{showId}")
	public String requestRemoveUser(@PathVariable("pageNum") Integer pageNum,@PathVariable("articleId") Integer articleId, 
		@PathVariable("showId") Integer showId, RedirectAttributes redirectAttributes, String keyword) throws UnsupportedEncodingException {
		Optional<Article> articleOptional = articleService.findArticleById(articleId);
		
		if (articleOptional.isEmpty()) {
			setModalMessage(redirectAttributes, "Error", "Cannot find Article with ID " + articleId);
		}
		else {
			setModalMessage(redirectAttributes, "Warning", "Are you sure to delete the article with ID " + articleId + "?");
			redirectAttributes.addFlashAttribute("articleId", articleId);
			redirectAttributes.addFlashAttribute("showId", showId);
			redirectAttributes.addFlashAttribute("yesButtonURL", 
					formatRedirectURL("/article/articles/remove/" + pageNum + "/" + articleId + "/" + showId, keyword));
		}
		
		return formatRedirectURL("redirect:/article/articles/" + pageNum, keyword);
	}
	
	@GetMapping("/article/articles/remove/{pageNum}/{articleId}/{showId}")
	public String removeUser(@PathVariable("pageNum") Integer pageNum, @PathVariable("articleId") Integer articleId,
		@PathVariable("showId") Integer showId, RedirectAttributes redirectAttributes, String keyword) throws UnsupportedEncodingException {
		
		articleService.deleteArticleById(articleId);
		AmazonS3Util.removeFolder("article-image-files/" + articleId);
//		FileUploadUtil.remove("article-image-files/" + articleId);
		
		if (showId == 0 && pageNum > 0) {
			pageNum--;
		}
		setModalMessage(redirectAttributes, "Success", "Successfully delete article with ID " + articleId);
		
		return formatRedirectURL("redirect:/article/articles/" + pageNum, keyword);
	}
	
	private String returnKeywordSuffixURL(String keyword) {
		if (keyword == null) {
				return "";
		}
		else {
				return "?keyword=" + keyword;
		}
	}
	
	private void setModalMessage(RedirectAttributes redirectAttributes, String modalTitle, String modalBody) {
		redirectAttributes.addFlashAttribute("modalTitle", modalTitle);
		redirectAttributes.addFlashAttribute("modalBody", modalBody);
	}
	
	private void updateTags(Article article, List<Tag> updatedTags) {
		List<Tag> tagCacheList = tagService.retrieveAllTags();
		Set<Tag> newTags = new TreeSet<>();
		updatedTags.forEach(updatedTag -> {
			if (tagCacheList.contains(updatedTag)) {
				Integer index = tagCacheList.indexOf(updatedTag);
				newTags.add(tagCacheList.get(index));
			}
			else {
				Tag tagInDB = tagService.createTag(updatedTag);
				tagCacheList.add(tagInDB);
				newTags.add(tagInDB);
			}
		});
		article.setTags(newTags);
	}
	
	private String formatRedirectURL(String redirectURL, String keyword) throws UnsupportedEncodingException {
		return (keyword == null) ? redirectURL : redirectURL + "?keyword=" + URLEncoder.encode(keyword, "utf-8");
	}
	
	private Page<Article> listArticlePage(Integer currentPage, String keyword) {
		if (keyword == null) {
			return articleService.listArticleByPage(currentPage);
		}
		else {
			return articleService.listArticleWithKeywordByPage(currentPage, keyword);
		}
	}
	
	// The format of tag string is "<tag>,<tag>, ...
	private List<Tag> parseTagString(String tagString) {
		List<Tag> tagList = new ArrayList<>();
		String[] tagStringArray = tagString.split(",");
		
		for (int count = 0; count < tagStringArray.length; count++) {
			tagList.add(new Tag(tagStringArray[count]));
		}
		
		return tagList;
	}
	
	private void uploadImageFiles(Integer articleId, MultipartFile[] imageFiles)
			throws IOException {
		String uploadDir = "article-image-files/" + articleId;
		AmazonS3Util.removeFolder(uploadDir);
//		FileUploadUtil.remove(uploadDir);
		for (int count = 0; count < imageFiles.length; count++) {
			if (!imageFiles[count].isEmpty()) {
				AmazonS3Util.uploadFile(uploadDir, imageFiles[count].getOriginalFilename(), imageFiles[count].getInputStream());
//				FileUploadUtil.saveFile(uploadDir, imageFiles[count].getOriginalFilename(), imageFiles[count]);
			}
		}
	}
	
}
