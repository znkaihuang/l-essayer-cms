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
import org.springframework.test.annotation.Rollback;

import com.lessayer.entity.Language;
import com.lessayer.entity.Lecturer;
import com.lessayer.entity.Video;

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
		Lecturer lecturer1 = new Lecturer("Lena", "Lin", "A professional English teacher devoting teaching for more than 10 years.");
		Lecturer lecturer2 = new Lecturer("Anne", " Le Grand", "A certified and experienced French instructor.");
		Lecturer lecturer3 = new Lecturer("Bo-nan", "Wu", "A passiontate speaker that enjoys sharing how to program.");
		
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
	public void createVideosTest() throws ParseException {
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
	public void listAllVideosTest() {
		List<Video> videoList = videoRepository.findAllVideos();
		
		assertThat(videoList.size()).isGreaterThan(0);
	}
	
}