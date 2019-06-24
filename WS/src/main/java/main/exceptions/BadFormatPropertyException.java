package main.exceptions;

public class BadFormatPropertyException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4276124791289333829L;

	public BadFormatPropertyException(String message) {
		super(message);
	}
	
	public BadFormatPropertyException(Exception exception) {
		super(exception);
	}
}
