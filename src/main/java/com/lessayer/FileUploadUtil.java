package com.lessayer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(FileUploadUtil.class);
	
	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile)
			throws IOException {
		Path uploadPath = Paths.get(uploadDir);
		
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		
		try (InputStream inpuStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inpuStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		}
		catch (IOException ex) {
			throw new IOException("Could not save file: " + fileName);
		}
		
	}
	
	public static void cleanDir(Path dirPath) {
		try {
			Files.list(dirPath).forEach(file -> {
				if (!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					} catch (IOException e) {
						logger.error("Could not delete file: " + file);
					}
				}
			});
		}
		catch (Exception e) {
			logger.error("Could not list directory: " + dirPath);
		}
	}
	
	public static void remove(String dir) {
		Path dirPath = Paths.get(dir);
		if (!Files.exists(dirPath)) {
			return;
		}
		
		cleanDir(dirPath);
		
		try {
			Files.delete(dirPath);
		}
		catch (Exception e) {
			logger.error("Could not remove directory: " + dir);
		}
	}
	
}
