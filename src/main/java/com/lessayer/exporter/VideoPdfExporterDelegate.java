package com.lessayer.exporter;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lessayer.AbstractFileExporter;
import com.lessayer.entity.Video;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class VideoPdfExporterDelegate {
	
	public static AbstractFileExporter<Video> getPdfExporter() throws DocumentException, IOException {
		return new VideoPdfExporter<>();
	}
	
}

class VideoPdfExporter<E> extends AbstractFileExporter<E> {
	
	private BaseFont baseFont;
	private Font headerFont;
	private Font labelFont;
	private Font dataFont;
	private Color lightGreen = Color.getHSBColor(0.33f, 1.0f, 0.7f);
	
	public VideoPdfExporter() throws DocumentException, IOException {
		baseFont = BaseFont.createFont("src/main/resources/static/font/NotoSerifCJK-Medium.ttc,0", 
				BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		headerFont = new Font(baseFont, 18, Font.BOLD, lightGreen);
		labelFont = new Font(baseFont, 12, Font.NORMAL, Color.WHITE);
		dataFont = new Font(baseFont, 12, Font.NORMAL, Color.DARK_GRAY);
	}
	
	@Override
	public void export(List<E> contentList, HttpServletResponse response) throws IOException {
		setResponseHeader(response, "application/pdf", ".pdf", "videos_");
		
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();
		
		Paragraph paragraph = new Paragraph("List of videos", headerFont);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(paragraph);
		
		for (E element : contentList) {
			PdfPTable table = new PdfPTable(8);
			table.setTotalWidth(1.0f);
			table.setSpacingBefore(20);
			
			Video video = (Video) element;
			PdfPCell titleLabelCell = createLabelCell("title", 1);
			PdfPCell titleCell = createDataCell(video.getTitle(), 7, 1);
			PdfPCell lecturerLabelCell = createLabelCell("lecturer", 1);
			PdfPCell lecturerCell = createDataCell(video.getLecturer().getName(), 7, 1);
			PdfPCell dateLabelCell = createLabelCell("uploaded date", 2);
			PdfPCell dateCell = createDataCell(video.getDateString(), 2, 1);
			PdfPCell uploaderLabelCell = createLabelCell("uploader", 2);
			PdfPCell uploaderCell = createDataCell(video.getUploader(), 2, 1);
			PdfPCell descriptionLabelCell = createLabelCell("description", 8);
			PdfPCell descriptionCell = createDataCell(video.getDescription(), 8, 3);
			PdfPCell languageLabelCell = createLabelCell("language", 2);
			PdfPCell languageCell = createDataCell(video.getLanguage().getLanguageName(), 2, 1);
			PdfPCell subtitleLabelCell = createLabelCell("subtitle", 1);
			PdfPCell subtitleCell = createDataCell(String.valueOf(video.isHasSubtitle()), 1, 1);
			PdfPCell lengthLabelCell = createLabelCell("length", 1);
			PdfPCell lengthCell = createDataCell(video.getVideoLength().toString(), 1, 1);
			PdfPCell tagsLabelCell = createLabelCell("tags", 1);
			PdfPCell tagsCell = createDataCell(video.getTagsString(), 7, 1);
			
			table.addCell(titleLabelCell);
			table.addCell(titleCell);
			table.addCell(lecturerLabelCell);
			table.addCell(lecturerCell);
			table.addCell(dateLabelCell);
			table.addCell(dateCell);
			table.addCell(uploaderLabelCell);
			table.addCell(uploaderCell);
			table.addCell(descriptionLabelCell);
			table.addCell(descriptionCell);
			table.addCell(languageLabelCell);
			table.addCell(languageCell);
			table.addCell(subtitleLabelCell);
			table.addCell(subtitleCell);
			table.addCell(lengthLabelCell);
			table.addCell(lengthCell);
			table.addCell(tagsLabelCell);
			table.addCell(tagsCell);
			
			table.completeRow();
			document.add(table);
		}

		document.close();
		
	}

	private PdfPCell createLabelCell(String label, int colSpanNum) {
		PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
		labelCell.setBackgroundColor(lightGreen);
		labelCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		if (colSpanNum > 1) {
			labelCell.setColspan(colSpanNum);
		}
		
		return labelCell;
	}
	
	private PdfPCell createDataCell(String data, int colSpanNum, int rowSpanNum) {
		PdfPCell dataCell = new PdfPCell(new Phrase(data, dataFont));
		dataCell.setColspan(colSpanNum);
		if (rowSpanNum > 1) {
			dataCell.setRowspan(rowSpanNum);
		}
		dataCell.setPaddingLeft(10.0f);
		return dataCell;
	}
	
}