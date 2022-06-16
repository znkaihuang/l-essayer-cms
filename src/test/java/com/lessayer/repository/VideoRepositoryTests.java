package com.lessayer.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;

import com.lessayer.entity.Language;
import com.lessayer.entity.Lecturer;
import com.lessayer.entity.Video;
import com.lessayer.filter.FilterHelper;
import com.lessayer.filter.FilterQueryObject;
import com.lessayer.service.VideoService;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class VideoRepositoryTests {
	
	@Autowired
	private VideoRepository videoRepository;
	
	@Autowired
	private LecturerRepository lecturerRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Test
	public void createLecturersTest() {
		Lecturer lecturer1 = new Lecturer("Lena Lin", "A professional English teacher devoting teaching for more than 10 years.");
		Lecturer lecturer2 = new Lecturer("Anne Le Grand", "A certified and experienced French instructor.");
		Lecturer lecturer3 = new Lecturer("Bo-nan Wu", "A passiontate speaker that enjoys sharing how to program.");
		
		lecturerRepository.save(lecturer1);
		lecturerRepository.save(lecturer2);
		lecturerRepository.save(lecturer3);
		
		System.out.println("Successfully creates lecturers!");
	}
	
	@Test
	public void listAllLecturersTest() {
		List<Lecturer> lecturerList = lecturerRepository.findAllLecturers();
		
		assertThat(lecturerList.size()).isGreaterThan(0);
	}
	
	@Test
	public void createVideosTest1() throws ParseException {
		Lecturer lecturer1 = entityManager.find(Lecturer.class, 1);
		Video video1 = new Video("Englisth speaking 1", "Lena", lecturer1, dateFormat.parse("2022-06-08"), 
				"Englist speaking class episode 1", "enlish_speaking_1.mp4", Language.EN, true, 420);
		
		Lecturer lecturer2 = entityManager.find(Lecturer.class, 2);
		Video video2 = new Video("French grammar 1", "Anne", lecturer2, dateFormat.parse("2022-06-09"), 
				"Enhance French grammar", "french_grammar_1.mp4", Language.FR, false, 500);
		
		Lecturer lecturer3 = entityManager.find(Lecturer.class, 3);
		Video video3 = new Video("Why you need to learn programming?", "Bo-nan", lecturer3, dateFormat.parse("2022-06-09"), 
				"Start to learn program series", "why_you_need_to_learn_programming.mp4", Language.ZH_TW, true, 600);
		
		videoRepository.save(video1);
		videoRepository.save(video2);
		videoRepository.save(video3);
		
		System.out.println("Successfully creates videos!");
	}
	
	@Test
	public void createVideosTest2() throws ParseException {
		Lecturer lecturer1 = entityManager.find(Lecturer.class, 1);
		Video video1 = new Video("Englisth speaking 2", "Lena", lecturer1, dateFormat.parse("2022-06-09"), 
				"Englist speaking class episode 2", "enlish_speaking_2.mp4", Language.EN, true, 435);
		
		Lecturer lecturer2 = entityManager.find(Lecturer.class, 2);
		Video video2 = new Video("French grammar 2", "Anne", lecturer2, dateFormat.parse("2022-06-11"), 
				"Enhance French grammar", "french_grammar_2.mp4", Language.FR, false, 480);
		Video video3 = new Video("French grammar 3", "Anne", lecturer2, dateFormat.parse("2022-06-14"), 
				"Enhance French grammar", "french_grammar_3.mp4", Language.FR, false, 515);
		
		Lecturer lecturer3 = entityManager.find(Lecturer.class, 3);
		Video video4 = new Video("Why you need to learn programming?", "Bo-nan", lecturer3, dateFormat.parse("2022-06-09"), 
				"Start to learn program series", "why_you_need_to_learn_programming.mp4", Language.ZH_TW, true, 600);
		Video video5 = new Video("How to learn programming?", "Bo-nan", lecturer3, dateFormat.parse("2022-06-12"), 
				"Start to learn program series", "how_to_learn_programming.mp4", Language.ZH_TW, true, 310);
		
		videoRepository.save(video1);
		videoRepository.save(video2);
		videoRepository.save(video3);
		videoRepository.save(video4);
		videoRepository.save(video5);
		
		System.out.println("Successfully creates videos!");
	}
	
	@Test
	public void listAllVideosTest() {
		List<Video> videoList = videoRepository.findAllVideos();
		
		assertThat(videoList.size()).isGreaterThan(0);
	}
	
	@Test
	public void findVideosWithLanguageTest() {
		List<Video> videoList = videoRepository.findVideosWithLanguage(Language.EN);
		printVideoList(videoList);
		assertThat(videoList.size()).isGreaterThan(0);
	}
	
	@Test
	public void findVideosBySubtitleTest() {
		List<Video> videoList = videoRepository.findVideosBySubtitle(true);
		printVideoList(videoList);
		assertThat(videoList.size()).isGreaterThan(0);
	}
	
	@Test
	public void findVideosBetweenLengthTest() {
		List<Video> videoList = videoRepository.findVideosBetweenLength(300, 500);
		printVideoList(videoList);
		assertThat(videoList.size()).isGreaterThan(0);
	}
	
	@Test
	public void findVideosWithFilterQueryTest() {
		List<FilterQueryObject> filterQueryList = getFilterQueryList("0-0,0-2,2-0,2-1");
		printFilterQueryList(filterQueryList);
		
		VideoService service = new VideoService(videoRepository);
		Page<Video> page = service.listVideosWithFilterByPage(0, filterQueryList);
		
		printVideoList(page.getContent());
		System.out.println(page.getTotalElements());
		System.out.println(page.getTotalPages());
	}
	
	@Test
	public void findVideosWithKeywordAndFilterQueryTest() {
		List<FilterQueryObject> filterQueryList = getFilterQueryList("0-0,0-2,2-0,2-1");
		printFilterQueryList(filterQueryList);
		
		String keyword = "06-09";
		
		VideoService service = new VideoService(videoRepository);
		Page<Video> page = service.listVideosWithKeywordAndFilterByPage(0, keyword, filterQueryList);
		
		printVideoList(page.getContent());
		System.out.println(page.getTotalElements());
		System.out.println(page.getTotalPages());
	}
	
	private List<FilterQueryObject> getFilterQueryList(String filterString) {
		String[] sections = {"Language", "Video Length", "Subtitle"};
		String[] options = {
				"English", "French", "Chinese",
				"0-10 minutes", "11-30 minutes", "31-60 minutes", "More than 1 hour",
				"With subtitle", "Without subtitle"
		};
		Integer[] optionNumPerSection = {3, 4, 2};
		FilterHelper filterHelper = new FilterHelper(sections, options, optionNumPerSection);
		filterHelper.setPrevFilterSelect("0-0");
		filterHelper.updateFilter(filterString);
		
		return filterHelper.getFilterQueryList();
	}
	
	private void printFilterQueryList(List<FilterQueryObject> queryList) {
		for (FilterQueryObject queryObject : queryList) {
			System.out.println("[" + queryObject.getSectionName() + "]");
			for (String queryOption : queryObject.getQueryOptions()) {
				System.out.print(queryOption + " ");
			}
			System.out.println();
		}
	}
	
	private void printVideoList(List<Video> videoList) {
		for (Video video : videoList) {
			System.out.println(video);
		}
	}
}
