package main.exceptions;

public class MissingConfigProperty extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6442229022379265383L;
	private static final String additionnalInfo = "The application.properties file can be located in the /config folder (at the project root level). To find more possible locations, please refer to this link --> https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config-profile-specific-properties";
	
	public MissingConfigProperty(String key) {		
		super(String.format("The file property key '%s' has not been loading from the externalized properties file\r\n\r\n%s\r\n", key, additionnalInfo));
	}
}
