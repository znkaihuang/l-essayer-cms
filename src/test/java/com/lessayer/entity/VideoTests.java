package com.lessayer.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.Test;

public class VideoTests {
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Test
	public void videoEntityTest() throws ParseException {
		Lecturer lecturer = new Lecturer("Lena", "Lin");
		Video video = new Video("Example vidoe", "lena", lecturer, dateFormat.parse("2022-06-06"), "An demonstrated example",
				"test.mp4", Language.EN, false, 30);
		
		System.out.println(video);
	}
	
}
