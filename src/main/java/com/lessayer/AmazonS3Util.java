package com.lessayer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

public class AmazonS3Util {
	
	private static final Logger logger = LoggerFactory.getLogger(AmazonS3Util.class);
	
	private static final String BUCKET_NAME;
	private static S3Client client = S3Client.builder().build();
	
	static {
		BUCKET_NAME = System.getenv("AWS_BUCKET_NAME");
	}
	
	public static List<String> listFolder(String folderName) {
		
		ListObjectsRequest listRequest = ListObjectsRequest.builder()
					.bucket(BUCKET_NAME).prefix(folderName).build();
		
		ListObjectsResponse response = client.listObjects(listRequest);
		
		List<S3Object> contents = response.contents();
		
		ListIterator<S3Object> listIterator = contents.listIterator();
		
		List<String> listKeys = new ArrayList<>();
		
		while (listIterator.hasNext()) {
			S3Object object = listIterator.next();
			logger.info(object.toString());
			listKeys.add(object.key());
		}
		
		return listKeys;
	}
	
	public static void uploadFile(String folderName, String fileName, InputStream inputStream) {
		
		PutObjectRequest putRequest = PutObjectRequest.builder()
				.bucket(BUCKET_NAME).key(folderName + "/" + fileName).acl("public-read").build();
		
		try (inputStream) {
			int contentLength = inputStream.available();
			client.putObject(putRequest, RequestBody.fromInputStream(inputStream, contentLength));
		}
		catch (IOException ex) {
			logger.error("Could not upload file to Amazon S3", ex);
		}
		
	}
	
	public static void deleteFile(String folderName, String fileName) {
		
		DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
				.bucket(BUCKET_NAME).key(folderName + "/" + fileName).build();
		client.deleteObject(deleteRequest);
		
	}
	
	public static void removeFolder(String folderName) {
		
		ListObjectsRequest listRequest = ListObjectsRequest.builder()
				.bucket(BUCKET_NAME).prefix(folderName + "/").build();

		ListObjectsResponse response = client.listObjects(listRequest);
		
		List<S3Object> contents = response.contents();
		
		ListIterator<S3Object> listIterator = contents.listIterator();
		
		while (listIterator.hasNext()) {
			S3Object object = listIterator.next();
			DeleteObjectRequest request = DeleteObjectRequest.builder().bucket(BUCKET_NAME)
							.key(object.key()).build();
			client.deleteObject(request);
		}
		
	}
	
}
