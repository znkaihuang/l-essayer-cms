package com.lessayer.exporter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.lessayer.AbstractFileExporter;
import com.lessayer.entity.Video;

public class VideoCsvExporterDelegate {

	public static AbstractFileExporter<Video> getCsvExporter() {
		return new VideoCsvExporter<>();
	}

}

class VideoCsvExporter<E> extends AbstractFileExporter<E> {
	
	@Override
	public void export(List<E> contentList, HttpServletResponse response) throws IOException {
		setResponseHeader(response, "text/csv", ".csv", "videos_");
		Writer writer = new OutputStreamWriter(response.getOutputStream(), "UTF8");
		
		ICsvListWriter csvWriter = new CsvListWriter(writer, CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = {"Video ID", "Title", "Lecturer", "Uploaded Date", "Uploader", "Description", "Language", "Subtitle", "Video Length"};

		csvWriter.writeHeader(csvHeader);
		
		for (E element : contentList) {
			if (element instanceof Video) {
				Video video = (Video) element;
				csvWriter.write(video.getId(), video.getTitle(), video.getLecturer().getName(), video.getDateString(), video.getUploader(),
						video.getDescription(), video.getLanguage().getLanguageName(), video.isHasSubtitle(), video.getVideoLength());
			}
		}
		
		csvWriter.close();
	}

}