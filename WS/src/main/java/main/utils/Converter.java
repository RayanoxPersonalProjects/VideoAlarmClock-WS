package main.utils;

public class Converter {
	
	public static String convertStacktraceToString(StackTraceElement [] stack) {
		String result = "";
		for (StackTraceElement stackTraceElement : stack) {
			result += stackTraceElement.toString() + "       ---------      " ;
		}
		return result;
	}
}
