package com.lessayer.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.lessayer.entity.Article;
import com.lessayer.entity.Tag;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ArticleRepositoryTests {
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private TagRepository tagRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	private static final String CURRENT_PATH = "src/test/java/com/lessayer/repository/";
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Test
	public void createTagTest() {
		Tag[] tags = new Tag[7];
		tags[0] = new Tag("思考");
		tags[1] = new Tag("健康");
		tags[2] = new Tag("單元測試");
		tags[3] = new Tag("程式");
		tags[4] = new Tag("Brésil");
		tags[5] = new Tag("le président de Petrobras");
		tags[6] = new Tag("limogé");
		
		for (Tag tag : tags) {
			tagRepository.save(tag);
		}
		
		long keywordCount = tagRepository.count();
		
		assertThat(keywordCount).isEqualTo(tags.length);
	}
	
	@Test
	public void createArticle1Test() throws IOException {
		String title = "負面思考不利健康";
		String author = "author1";
		String summary = "情緒在調節影響我們健康的身體系統上，扮演重要的角色。";
		String content = readFile("article1.md");
		Set<Tag> tags = new HashSet<>();
		
		tags.add(entityManager.find(Tag.class, 1));
		tags.add(entityManager.find(Tag.class, 2));
		
		Article article = new Article(title, author, new Date(), summary, content, tags);
		
		Article savedArticle = articleRepository.save(article);
		
		assertThat(savedArticle.getTitle()).isEqualTo(title);
	}
	
	@Test
	public void createArticle2Test() throws IOException {
		String title = "單元測試-概念";
		String author = "author2";
		String summary = "介紹單元測試。";
		String content = readFile("article2.md");
		Set<Tag> tags = new HashSet<>();
		
		tags.add(entityManager.find(Tag.class, 3));
		tags.add(entityManager.find(Tag.class, 4));
		
		Article article = new Article(title, author, new Date(), summary, content, tags);
		
		Article savedArticle = articleRepository.save(article);
		
		assertThat(savedArticle.getTitle()).isEqualTo(title);
	}
	
	@Test
	public void createArticle3Test() throws IOException {
		String title = "Brésil: le président de Petrobras limogé 40 jours après avoir été nommé";
		String author = "author3";
		String summary = "Le président brésilien Jair Bolsonaro a limogé lundi José Mauro Coelho, nommé il y a seulement 40 jours. "
				+ "Le gouvernement l'a remercié pour son travail, sans préciser les raisons de cette décision. "
				+ "Jair Bolsonaro s'élève depuis plusieurs mois contre la hausse du prix du pétrole et les bénéfices records de Petrobras.";
		String content = readFile("article3.md");
		Set<Tag> tags = new HashSet<>();
		
		tags.add(entityManager.find(Tag.class, 5));
		tags.add(entityManager.find(Tag.class, 6));
		tags.add(entityManager.find(Tag.class, 7));
		
		Article article = new Article(title, author, new Date(), summary, content, tags);
		
		Article savedArticle = articleRepository.save(article);
		
		assertThat(savedArticle.getTitle()).isEqualTo(title);
	}
	
	@Test
	public void createArticle4Test() throws IOException {
		String title = "健康食品吃過量恐傷身？！醫教三原則這樣吃更安心！";
		String author = "author1";
		String summary = "為了讓身體更加健康，不少人除了保持良好的運動習慣、飲食習慣、生活習慣等，也會使用健康食品來做輔助。"
				+ "但健康食品並不適多吃多健康唷，過量的使用恐怕會對身體帶來負擔，補到後來變傷身！";
		String content = readFile("article4.md");
		Set<Tag> tags = new HashSet<>();
		
		Tag newTag = new Tag("健康食品");
		tagRepository.save(newTag);
		
		tags.add(entityManager.find(Tag.class, 2));
		tags.add(newTag);
		
		Article article = new Article(title, author, new Date(), summary, content, tags);
		
		Article savedArticle = articleRepository.save(article);
		
		assertThat(savedArticle.getTitle()).isEqualTo(title);
	}
	
	@Test
	public void createArticle5Test() throws IOException {
		String title = "常常疲倦、流目油？中醫：恐是肝功能異常，3招助改善";
		String author = "author1";
		String summary = "你常常身體疲倦或眼睛累的時候流目油、流眼淚嗎？在中醫的觀點上來看，可能是肝臟功能開始出現異常，要多注意囉！";
		String content = readFile("article5.md");
		Set<Tag> tags = new HashSet<>();
		
		Tag newTag1 = new Tag("疲倦");
		Tag newTag2 = new Tag("肝臟");
		tagRepository.save(newTag1);
		tagRepository.save(newTag2);
		
		tags.add(entityManager.find(Tag.class, 2));
		tags.add(newTag1);
		tags.add(newTag2);
		
		Article article = new Article(title, author, new Date(), summary, content, tags);
		
		Article savedArticle = articleRepository.save(article);
		
		assertThat(savedArticle.getTitle()).isEqualTo(title);
	}
	
	@Test
	public void findArticlesWithAuthorTest() {
		Pageable page = PageRequest.of(0, 5);
		String autherName = "author1";
		Page<Article> articlePage = articleRepository.findArticlesWithAuthor(autherName, page);
		List<Article> articleList = articleRepository.findArticlesWithAuthor(autherName);
		
		printArticleList(articleList);
		
		assertThat(articlePage.getContent().get(0).getAuthor()).isEqualTo(autherName);
		assertThat(articleList.get(0).getAuthor()).isEqualTo(autherName);
	}
	
	@Test
	public void findArticlesBeforeDateTest() throws ParseException {
		Pageable page = PageRequest.of(0, 5);
		Date date = dateFormat.parse("2022-05-23");
		Page<Article> articlePage = articleRepository.findArticlesBeforeDate(date, page);
		List<Article> articleList = articleRepository.findArticlesBeforeDate(date);
		
		printArticleList(articleList);
		
		assertThat(articlePage.getContent().get(0).getDate()).isBefore(date);
		assertThat(articleList.get(0).getDate()).isBefore(date);
	}
	
	@Test
	public void findArticlesEqualDateTest() throws ParseException {
		Pageable page = PageRequest.of(0, 5);
		Date date = dateFormat.parse("2022-05-24");
		Page<Article> articlePage = articleRepository.findArticlesEqualDate(date, page);
		List<Article> articleList = articleRepository.findArticlesEqualDate(date);
		
		printArticleList(articleList);
		
		assertThat(articlePage.getContent().get(0).getDate()).isInSameDayAs(date);
		assertThat(articleList.get(0).getDate()).isInSameDayAs(date);
	}
	
	@Test
	public void findArticlesAfterDateTest() throws ParseException {
		Pageable page = PageRequest.of(0, 5);
		Date date = dateFormat.parse("2022-05-21");
		Page<Article> articlePage = articleRepository.findArticlesAfterDate(date, page);
		List<Article> articleList = articleRepository.findArticlesAfterDate(date);
		
		printArticleList(articleList);
		
		assertThat(articlePage.getContent().get(0).getDate()).isAfter(date);
		assertThat(articleList.get(0).getDate()).isAfter(date);
	}
	
	@Test
	public void findArticlesBetweenDatesTest() throws ParseException {
		Pageable page = PageRequest.of(0, 5);
		Date beginDate = dateFormat.parse("2022-05-21");
		Date endDate = dateFormat.parse("2022-05-23");
		Page<Article> articlePage = articleRepository.findArticlesBetweenDates(beginDate, endDate, page);
		List<Article> articleList = articleRepository.findArticlesBetweenDates(beginDate, endDate);
		
		printArticleList(articleList);
		printArticleList(articlePage.getContent());
	}
	
	@Test
	public void findArticlesWithTagTest() {
		Pageable page = PageRequest.of(0, 5);
		Tag tag = entityManager.find(Tag.class, 2); // 健康
		Page<Article> articlePage = articleRepository.findArticlesWithTag(tag.getTag(), page);
		List<Article> articleList = articleRepository.findArticlesWithTag(tag.getTag());
		
		printArticleList(articleList);
		printArticleList(articlePage.getContent());
	}
	
	@Test
	public void findArticlesWithKeywordTest() {
		Pageable page = PageRequest.of(0, 5);
		String keyword1 = "président";
		String keyword2 = "Mary";
		Page<Article> articlePage1 = articleRepository.findArticlesWithKeyword(keyword1, page);
		List<Article> articleList1 = articleRepository.findArticlesWithKeyword(keyword1);
		Page<Article> articlePage2 = articleRepository.findArticlesWithKeyword(keyword2, page);
		List<Article> articleList2 = articleRepository.findArticlesWithKeyword(keyword2);
		
		printArticleList(articleList1);
		printArticleList(articlePage1.getContent());
		printArticleList(articleList2);
		printArticleList(articlePage2.getContent());
		
		assertThat(articleList1.size()).isGreaterThan(0);
		assertThat(articlePage1.getContent().size()).isGreaterThan(0);
		assertThat(articleList2.size()).isEqualTo(0);
		assertThat(articlePage2.getContent().size()).isEqualTo(0);
	}
	
	@Test
	public void updateTagTest() {
		String newTag = "Health";
		tagRepository.updateTag(2, newTag);
		Tag tagInDB = tagRepository.findById(2).get();
		
		assertThat(tagInDB.getTag()).isEqualTo(newTag);
	}
	
	private void printArticleList(List<Article> articleList) {
		articleList.forEach(article -> {
			System.out.println("ID: " + article.getId() + " Title: " + article.getTitle());
			System.out.println("Author: " + article.getAuthor() + " Date: " + article.getDate());
			System.out.print("Tag: ");
			article.getTags().forEach(keyword -> {
				System.out.print(keyword.getTag() + " ");
			});
			System.out.println();
		});
	}
	
	private String readFile(String fileName) throws IOException {
		StringBuilder content = new StringBuilder();
		FileReader fileReader = new FileReader(CURRENT_PATH + fileName);
		
		try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
			String str;
			while ( (str = bufferedReader.readLine()) != null ) {
				content.append(str).append("\n");
			}
		}
		
		return content.toString();
	}
	
}
