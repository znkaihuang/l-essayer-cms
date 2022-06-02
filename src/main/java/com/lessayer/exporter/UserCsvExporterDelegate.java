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
		Writer writer = new OutputStreamWriter(response.getOutputStream(), "UTF8");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"User ID", "E-mail", "First Name", "Last Name", "Roles", "Enabled", "Registration Date"};
		String[] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled", "registrationDate"};
		
		csvWriter.writeHeader(csvHeader);
		
		for (E user : contentList) {
			if (user instanceof User) {
				csvWriter.write((User) user, fieldMapping);
			}
		}
		
		csvWriter.close();
	}

}
