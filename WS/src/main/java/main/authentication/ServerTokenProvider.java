package main.authentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import main.exceptions.MissingConfigProperty;

@Component
public class ServerTokenProvider {

	private final String DEFAULT_SERVER_TOKEN = "test";
	
	@Value(value = "${token_server_side}")
	private String serverToken;
	
	public ServerTokenProvider() {
		
	}
	
	public boolean AuthorizeRequestToken(String token) {
		try {
			return getServerToken().equals(token);
		}catch (MissingConfigProperty e) {
			e.printStackTrace();
			System.err.println("\r\nThe server side token has not been loaded. So every incoming requests are rejected.\r\n");
			return false;
		}
	}
	
	public String getPassword() {
		return serverToken;
	}
	
	
	private String getServerToken() throws MissingConfigProperty {		
		if(serverToken == null)
			throw new MissingConfigProperty("token_server_side");
		
		if(serverToken.equals(DEFAULT_SERVER_TOKEN))
			System.out.println("The server token is set to the default value 'test'. Please change it to ensure a better security access.");
		
		return serverToken;
	}
	
	
}
