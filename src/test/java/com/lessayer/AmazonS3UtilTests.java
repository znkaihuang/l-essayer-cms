package com.lessayer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Test;

public class AmazonS3UtilTests {
	
	@Test
	public void testListFolder() {
		String folderName = "videos";
		List<String> listKeys = AmazonS3Util.listFolder(folderName);
		listKeys.forEach(System.out::println);
	}
	
	@Test
	public void testUploadFile() throws FileNotFoundException {
		String folderName = "videos";
		String fileName = "snapplet";
		String filePath = "/home/zkhuang/文件/" + fileName;

		InputStream inputStream = new FileInputStream(filePath);

		AmazonS3Util.uploadFile(folderName, fileName, inputStream);
	}
	
	@Test
	public void testDeleteFile() {
		String folderName = "videos";
		String fileName = "snapplet";;

		AmazonS3Util.deleteFile(folderName, fileName);
	}
	
	@Test
	public void testRemoveFolder() {
		String folderName = "test-upload";
		AmazonS3Util.removeFolder(folderName);
	}
	
}
