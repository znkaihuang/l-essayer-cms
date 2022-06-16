package com.lessayer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(FileUploadUtil.class);
	
	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile)
			throws IOException {
		saveFile(uploadDir, fileName, multipartFile, false);
	}
	
	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile, Boolean compressed)
			throws IOException {
		Path uploadPath = Paths.get(uploadDir);
		
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		
		try (InputStream inputStream = multipartFile.getInputStream()) {
			if (compressed == false) {
				Path filePath = uploadPath.resolve(fileName);
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			}
			else {
				Path filePath = uploadPath.resolve(fileName.replaceAll("[.].*", ".bz2"));
				OutputStream out = new FileOutputStream(filePath.toFile());
				BZip2CompressorOutputStream bzOut = new BZip2CompressorOutputStream(out);
				final byte[] buffer = new byte[100];
				int n = 0;
				while (-1 != (n = inputStream.read(buffer))) {
				    bzOut.write(buffer, 0, n);
				}
				bzOut.close();
			}
		}
		catch (IOException ex) {
			throw new IOException("Could not save file: " + fileName + ".\n" + ex);
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
