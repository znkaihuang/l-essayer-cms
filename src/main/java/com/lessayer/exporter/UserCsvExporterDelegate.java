package com.lessayer.exporter;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.lessayer.AbstractFileExporter;
import com.lessayer.entity.User;

public class UserCsvExporterDelegate {
	
	public static AbstractFileExporter<User> getCsvExporter() {
		return new UserCsvExporter<>();
	}
	
}

class UserCsvExporter<E> extends AbstractFileExporter<E> {
	
	@Override
	public void export(List<E> contentList, HttpServletResponse response) throws IOException {
		
		setResponseHeader(response, "text/csv", ".csv", "users_");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), 
				CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"User ID", "E-mail", "First Name", "Last Name", "Roles", "Enabled", "Registration Date"};
		String[] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled", "registrationDate"};
		
		csvWriter.writeHeader(csvHeader);
		
		for (E staff : contentList) {
			if (staff instanceof User) {
				csvWriter.write((User) staff, fieldMapping);
			}
		}
		
		csvWriter.close();
		
		
	}

}
