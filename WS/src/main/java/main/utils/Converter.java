package main.utils;

import java.sql.Time;
import java.util.Calendar;

import main.exceptions.BadFormatPropertyException;

public class Converter {
	
	public static String convertStacktraceToString(StackTraceElement [] stack) {
		String result = "";
		for (StackTraceElement stackTraceElement : stack) {
			result += stackTraceElement.toString() + "       ---------      " ;
		}
		return result;
	}
	
	/**
	 * Convert a date according to the french date format
	 * 
	 * @param dateString
	 * @return
	 * @throws Exception 
	 */
	public static Time convertStringToCalendar(String dateString) throws Exception {
		String errorMessage = "The string date does not have the correct synthax (hh:mm:ss)";
		try {
			Time time = Time.valueOf(dateString); // TODO Tester qu'une exception se throw bien si le format est mauvais, ou l'heure incorrecte.
			return time;
		}catch(Exception e) {
			throw new Exception(errorMessage);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static String convertTimeToString(Time time) {
		return String.format("%:%d:%d", time.getHours(),time.getMinutes(), time.getSeconds());
	}
}
