package com.lessayer.exporter;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lessayer.AbstractFileExporter;
import com.lessayer.entity.Article;
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

public class ArticlePdfExporterDelegate {
	
	public static AbstractFileExporter<Article> getPdfExporter() throws DocumentException, IOException {
		return new ArticlePdfExporter<>();
	}
	
}

class ArticlePdfExporter<E> extends AbstractFileExporter<E> {
	
	private BaseFont baseFont;
	private Font headerFont;
	private Font labelFont;
	private Font dataFont;
	private Color lightGreen = Color.getHSBColor(0.33f, 1.0f, 0.7f);
	
	public ArticlePdfExporter() throws DocumentException, IOException {
		baseFont = BaseFont.createFont("src/main/resources/static/font/NotoSerifCJK-Medium.ttc,0", 
				BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		headerFont = new Font(baseFont, 18, Font.BOLD, lightGreen);
		labelFont = new Font(baseFont, 12, Font.NORMAL, Color.WHITE);
		dataFont = new Font(baseFont, 12, Font.NORMAL, Color.DARK_GRAY);
	}
	
	@Override
	public void export(List<E> contentList, HttpServletResponse response) throws IOException {
		setResponseHeader(response, "application/pdf", ".pdf", "articles_");
		
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();
		
		Paragraph paragraph = new Paragraph("List of articles", headerFont);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(paragraph);
		
		for (E element : contentList) {
			PdfPTable table = new PdfPTable(8);
			table.setTotalWidth(1.0f);
			table.setSpacingBefore(20);
			
			Article article = (Article) element;
			PdfPCell titleLabelCell = createLabelCell("title", 1);
			PdfPCell titleCell = createDataCell(article.getTitle(), 7, 1);
			PdfPCell authorLabelCell = createLabelCell("author", 1);
			PdfPCell authorCell = createDataCell(article.getAuthor(), 3, 1);
			PdfPCell dateLabelCell = createLabelCell("published date", 2);
			PdfPCell dateCell = createDataCell(article.getDateString(), 2, 1);
			PdfPCell summaryLabelCell = createLabelCell("summary", 8);
			PdfPCell summaryCell = createDataCell(article.getSummary(), 8, 3);
			PdfPCell tagsLabelCell = createLabelCell("tags", 1);
			PdfPCell tagsCell = createDataCell(article.getTagsString(), 7, 1);
			
			table.addCell(titleLabelCell);
			table.addCell(titleCell);
			table.addCell(authorLabelCell);
			table.addCell(authorCell);
			table.addCell(dateLabelCell);
			table.addCell(dateCell);
			table.addCell(summaryLabelCell);
			table.addCell(summaryCell);
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