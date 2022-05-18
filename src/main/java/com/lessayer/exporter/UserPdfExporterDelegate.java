package com.lessayer.exporter;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lessayer.AbstractFileExporter;
import com.lessayer.entity.User;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class UserPdfExporterDelegate {
	
	public static AbstractFileExporter<User> getPdfExporter() {
		return new UserPdfExporter<>();
	}
	
}

class UserPdfExporter<E> extends AbstractFileExporter<E> {

	@Override
	public void export(List<E> contentList, HttpServletResponse response) throws IOException {
		
		setResponseHeader(response, "application/pdf", ".pdf", "staffs_");
		
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		
		document.open();
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.getHSBColor(0.33f, 1.0f, 0.7f));
		
		Paragraph paragraph = new Paragraph("List of staffs", font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(paragraph);
		
		List<String> headerList = new ArrayList<>();
		PdfPTable table = configTableSettings(headerList);
		writeTableHeader(table, headerList);
		writeTableData(table, contentList);
		
		document.add(table);
		
		document.close();
		
	}
	
	private PdfPTable configTableSettings(List<String> headerList) {
		headerList.add("ID");
		headerList.add("E-mail");
		headerList.add("First Name");
		headerList.add("Last Name");
		headerList.add("Rolse");
		headerList.add("Enabled");
		headerList.add("Registration");		
		
		PdfPTable table = new PdfPTable(headerList.size());
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		table.setWidths(new float[] {1.0f, 3.0f, 2.0f, 2.0f, 2.5f, 1.7f, 2.0f});
		
		return table;
	}

	private void writeTableData(PdfPTable table, List<E> contentList) {
		
		if (contentList.get(0) instanceof User) {
			for (E element : contentList) {
				User user = (User) element;
				table.addCell(String.valueOf(user.getId()));
				table.addCell(String.valueOf(user.getEmail()));
				table.addCell(String.valueOf(user.getFirstName()));
				table.addCell(String.valueOf(user.getLastName()));
				table.addCell(String.valueOf(user.getRoles().toString()));
				table.addCell(String.valueOf(user.isEnabled()));
				table.addCell(String.valueOf(user.getRegistrationDate()));
			}
		}
	}

	private void writeTableHeader(PdfPTable table, List<String> headerList) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.getHSBColor(0.33f, 1.0f, 0.7f));
		cell.setPadding(5);
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.white);
		
		headerList.forEach(header -> {
			cell.setPhrase(new Phrase(header, font));
			table.addCell(cell);
		});
	}
	
}