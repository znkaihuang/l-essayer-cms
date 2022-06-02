package com.lessayer.iText;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lessayer.entity.Tag;
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

public class iTextTests {

	private BaseFont baseFont;
	private OutputStream fileOutputStream;

	@BeforeEach
	public void setBaseFont() throws DocumentException, IOException {
		baseFont = BaseFont.createFont("src/main/resources/static/font/NotoSerifCJK-Medium.ttc,0", BaseFont.IDENTITY_H,
				BaseFont.NOT_EMBEDDED);
		fileOutputStream = new FileOutputStream("src/test/java/com/lessayer/iText/test.pdf");
	}

	@Test
	public void createTableTest() throws FileNotFoundException {
		String title = "負面思考不利健康";
		String author = "author1";
		String date = "2022-06-01";
		String summary = "你常常身體疲倦或眼睛累的時候流目油、流眼淚嗎？在中醫的觀點上來看，可能是肝臟功能開始出現異常，要多注意囉！";
		Set<Tag> tags = new TreeSet<>();
		tags.add(new Tag("思考"));
		tags.add(new Tag("健康"));

		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, fileOutputStream);
		document.open();

		PdfPTable table = new PdfPTable(8);
		table.setTotalWidth(1.0f);

		Font labelFont = new Font(baseFont);
		labelFont.setColor(Color.WHITE);
		Font dataFont = new Font(baseFont);
		dataFont.setColor(Color.DARK_GRAY);

		PdfPCell titleLableCell = new PdfPCell(new Phrase("title", labelFont));
		titleLableCell.setBackgroundColor(Color.getHSBColor(0.33f, 1.0f, 0.7f));
		titleLableCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

		PdfPCell titleCell = new PdfPCell(new Phrase(title, dataFont));
		titleCell.setColspan(7);
		titleCell.setPaddingLeft(10.0f);

		PdfPCell authorLableCell = new PdfPCell(new Phrase("author", labelFont));
		authorLableCell.setBackgroundColor(Color.getHSBColor(0.33f, 1.0f, 0.7f));
		authorLableCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

		PdfPCell authorCell = new PdfPCell(new Phrase(author, dataFont));
		authorCell.setColspan(3);
		authorCell.setPaddingLeft(10.0f);

		PdfPCell dateLableCell = new PdfPCell(new Phrase("published date", labelFont));
		dateLableCell.setBackgroundColor(Color.getHSBColor(0.33f, 1.0f, 0.7f));
		dateLableCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		dateLableCell.setColspan(2);

		PdfPCell dateCell = new PdfPCell(new Phrase(date, dataFont));
		dateCell.setColspan(2);
		dateCell.setPaddingLeft(10.0f);

		PdfPCell summaryLableCell = new PdfPCell(new Phrase("summary", labelFont));
		summaryLableCell.setBackgroundColor(Color.getHSBColor(0.33f, 1.0f, 0.7f));
		summaryLableCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		summaryLableCell.setColspan(8);
		summaryLableCell.setRowspan(2);

		PdfPCell summaryCell = new PdfPCell(new Phrase(summary, dataFont));
		summaryCell.setColspan(8);
		summaryCell.setRowspan(3);
		summaryCell.setPaddingLeft(10.0f);

		table.addCell(titleLableCell);
		table.addCell(titleCell);
		table.addCell(authorLableCell);
		table.addCell(authorCell);
		table.addCell(dateLableCell);
		table.addCell(dateCell);
		table.addCell(summaryLableCell);
		table.addCell(summaryCell);
		table.completeRow();

		Paragraph paragraph = new Paragraph(
				"**A 	a 	(À 	à) 	(Â 	â) 	B 	b 	C 	c 	(Ç 	ç) 	D 	d 	E 	e 	(É 	é) 	(È 	è) 	(Ê 	ê) 	(Ë 	ë) 	F 	f 	G 	g 	H 	h\n"
				+ "I 	i 	(Î 	î) 	(Ï 	ï) 	J 	j 	K 	k 	L 	l 	M 	m 	N 	n 	O 	o 	(Ô 	ô) 	P 	p 	Q 	q 	R 	r 	S 	s 	T 	t\n"
				+ "U 	u 	(Û 	û) 	(Ù 	ù) 	(Ü 	ü) 	V 	v 	W 	w 	X 	x 	Y 	y 	(Ÿ 	ÿ) 	Z 	z 	  **",
				dataFont);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);

		document.add(table);
		document.add(paragraph);
		document.close();

	}

}
