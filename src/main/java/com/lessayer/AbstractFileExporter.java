package com.lessayer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

public abstract class AbstractFileExporter<E> {
	
	public abstract void export(List<E> contentList, HttpServletResponse response) throws IOException;
	
	public void setResponseHeader(HttpServletResponse response, String contentType,
		String extension, String prefix) throws IOException {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timestamp = dateFormatter.format(new Date());
		String fileName = prefix + timestamp + extension;
		
		response.setContentType(contentType);
		response.setCharacterEncoding("utf-8");
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		response.setHeader(headerKey, headerValue);
	}
	
}
