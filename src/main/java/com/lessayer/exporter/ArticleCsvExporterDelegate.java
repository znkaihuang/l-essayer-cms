package com.lessayer.exporter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.lessayer.AbstractFileExporter;
import com.lessayer.entity.Article;

public class ArticleCsvExporterDelegate {

	public static AbstractFileExporter<Article> getCsvExporter() {
		return new ArticleCsvExporter<>();
	}

}

class ArticleCsvExporter<E> extends AbstractFileExporter<E> {
	
	@Override
	public void export(List<E> contentList, HttpServletResponse response) throws IOException {
		setResponseHeader(response, "text/csv", ".csv", "articles_");
		Writer writer = new OutputStreamWriter(response.getOutputStream(), "UTF8");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"Article ID", "Title", "Author", "Published Date", "Summary", "Tags"};
		String[] fieldMapping = {"id", "title", "author", "date", "summary", "tags"};
		
		csvWriter.writeHeader(csvHeader);
		
		for (E user : contentList) {
			if (user instanceof Article) {
				csvWriter.write((Article) user, fieldMapping);
			}
		}
		
		csvWriter.close();
	}

}