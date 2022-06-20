package com.lessayer.entity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.junit.jupiter.api.Test;

public class VideoTests {
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Test
	public void videoEntityTest() throws ParseException {
		Lecturer lecturer = new Lecturer("Lena", "Lin");
		Set<Tag> tags = new TreeSet<>();
		tags.add(new Tag("example"));
		Video video = new Video("Example vidoe", "lena", lecturer, dateFormat.parse("2022-06-06"), "An demonstrated example",
				"test.mp4", Language.EN, false, 30);
		video.setTags(tags);
		
		System.out.println(video);
		System.out.println(video.getDateString());
		System.out.println(video.getTagsString());
		System.out.println(video.getLecturer().getName());
		System.out.println(video.getCoverImagePath());
		System.out.println(video.getVideoPath());
	}
	
	@Test
	public void mediaSizeTest() throws IOException {
		Path path = Paths.get("src/test/java/com/lessayer/repository/example/videos/test.mp4");
		
		System.out.println("Size: " + Files.size(path) + " Bytes");
	}
	
	@Test
	public void compressFileTest() throws IOException {
		InputStream in = Files.newInputStream(Paths.get("src/test/java/com/lessayer/repository/example/videos/test.mp4"));
		OutputStream out = Files.newOutputStream(Paths.get("src/test/java/com/lessayer/repository/example/videos/test.bz2"));
		BufferedOutputStream bufferedOut = new BufferedOutputStream(out);
		BZip2CompressorOutputStream bzOut = new BZip2CompressorOutputStream(bufferedOut);
		final byte[] buffer = new byte[100];
		int n = 0;
		while (-1 != (n = in.read(buffer))) {
		    bzOut.write(buffer, 0, n);
		}
		bzOut.close();
		in.close();
		
		System.out.println("Fininsh compressing file!");
	}
	
	@Test
	public void uncompressFileTest() throws IOException {
		InputStream fin = Files.newInputStream(Paths.get("src/test/java/com/lessayer/repository/example/videos/test.bz2"));
		BufferedInputStream in = new BufferedInputStream(fin);
		OutputStream out = Files.newOutputStream(Paths.get("src/test/java/com/lessayer/repository/example/videos/uncompressed.mp4"));
		BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(in);
		final byte[] buffer = new byte[100];
		int n = 0;
		while (-1 != (n = bzIn.read(buffer))) {
		    out.write(buffer, 0, n);
		}
		out.close();
		bzIn.close();
		
		System.out.println("Fininsh uncompressing file!");
	}
	
}
