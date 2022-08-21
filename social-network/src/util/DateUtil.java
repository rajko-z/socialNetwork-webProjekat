package util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
	
	DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public LocalDate parseStringToLocalDate(String date) {
		return LocalDate.parse(date, localDateFormatter);
	}
	
	public LocalDateTime parseStringToLocalDateTime(String date) {
		return LocalDateTime.parse(date, localDateTimeFormatter);
	}

	public String getStringFormatFromLocalDateTime(LocalDateTime date) {
		return date.format(localDateTimeFormatter);
	}
}
