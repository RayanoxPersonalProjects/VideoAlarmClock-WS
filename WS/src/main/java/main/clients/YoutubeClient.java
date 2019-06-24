package main.clients;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.TasksScopes;

import main.exceptions.BadFormatPropertyException;

@Service
public class YoutubeClient implements IYoutubeClient {

	private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

	private static final String APPLICATION_NAME = "RegularTasks-mediation";
	private static final List<String> SCOPES = Collections.singletonList(TasksScopes.TASKS);
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";

	private Tasks taskService;

	public YoutubeClient() throws GeneralSecurityException, IOException {

//		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//		taskService = new Tasks.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//				.setApplicationName(APPLICATION_NAME).build();
	}


	
	
	
	
	
	/*
	 * Private functions
	 */




	private HashMap<String, String> extractNotesValues(String notes) throws BadFormatPropertyException {
		HashMap<String, String> result = new HashMap<String, String>();
		String [] properties = notes.split(";");
		for (String line : properties) {
			String [] lineSplitted = line.split("=");
			if(lineSplitted.length != 2)
				throw new BadFormatPropertyException("The property line in the Task 'note' doesn't have the good format (format: key=value). ---> lineSplitted.length = " + lineSplitted.length);
			String key = lineSplitted[0];
			String value = lineSplitted[1];
			result.put(key, value);
		}
		return result;
	}



	private String getDayDateString(Calendar calendar) {
		String patternDate = "%d-%d-%dT00:00:00+00:00";

		return String.format(patternDate, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH));
	}
	
	
	/**
	 * Creates an authorized Credential object.
	 * 
	 * @param HTTP_TRANSPORT The network HTTP Transport.
	 * @return An authorized Credential object.
	 * @throws IOException If the credentials.json file cannot be found.
	 */
	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		// Load client secrets.
		InputStream in = YoutubeClient.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		if (in == null) {
			String errorMessage = "Please add the '" + CREDENTIALS_FILE_PATH
					+ "' file to the resource folder (src/main/resources)";
			throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES)
						.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
						.setAccessType("offline").build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}

}
