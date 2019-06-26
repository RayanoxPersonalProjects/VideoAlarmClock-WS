package main.controllers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import main.authentication.ServerTokenProvider;
import main.model.ContentType;
import main.model.ErrorBuilder;
import main.providers.DataStorage;
import main.providers.IDataProvider;
import main.providers.InfoChannel;
import main.utils.Converter;

@RestController
public class DriveController {

	private static final String CONF_VOLUM_PROPERTY_NAME = "volumLevel";
	private final Integer DEFAULT_VOLUM = 30;
	
	private final String CONF_ALARM_CLOCK_PROPERTY_NAME = "alarmClockTime";
	private final Time DEFAULT_ALARM_TIME = new Time(7, 30, 0);
	
	
	@Autowired
	ServerTokenProvider serverTokenProvider;
	
	@Autowired
	IDataProvider dataProvider;

	@Autowired
	private DataStorage dataStorage;

    @RequestMapping(value = "/getCommands", method = RequestMethod.GET)
    public String GetCommands(@RequestParam(value = "token") String token, char contentTypeCode) throws AuthenticationException {
    	String failedAuthMessage = processAuthorization(token);
    	if(failedAuthMessage != null)
    		return failedAuthMessage;
    	
    	ContentType contentType = ContentType.valueOf(contentTypeCode);
    	
    	Calendar currentDate = Calendar.getInstance();
    	
    	String result = String.format("(Content type = %s) - ", contentType.toString());
    	try {
    		String commands = "";
    		switch(contentType) {
    			case INFO_CHANNEL :
					int dayOfYear = currentDate.get(Calendar.DAY_OF_YEAR);
					InfoChannel channelSelected = dayOfYear  % 2 == 0 ? InfoChannel.France24 : InfoChannel.euronews_playlist;
    				commands = dataProvider.GetContentCommand_InfoChannel(channelSelected);
    				result += String.format("[channelSelected = %s , DAY_O_Y = %s, command length = %d] - ", channelSelected.toString(), dayOfYear, ((commands.length()+1)/2) - 2);
    				break;
    			case CUSTOM_DAILY_YOUTUBE_PLAYLIST:
    				commands = dataProvider.GetContentCommand_CustomDailyYoutubePlaylist();
    				break;
    			default:
    				result += ErrorBuilder.buildError("The content type requested by the parameter contentTypeCode in the 'GetCommand' WS method does not exist in the system. Please request an other operation type");
    		}
    		return result + commands;
    	}catch(Exception e) {
    		return result + ErrorBuilder.buildError(formatStringException(e));
    	}
    }
    
    @GetMapping(value = "/getTimeAlarm")
    public String GetTimeAlarm(@RequestParam(value = "token") String token) throws AuthenticationException {
    	String failedAuthMessage = processAuthorization(token);
    	if(failedAuthMessage != null)
    		return failedAuthMessage;
    	
    	try {
    		Time alarmTime = (Time) this.dataStorage.getData(CONF_ALARM_CLOCK_PROPERTY_NAME, Time.class);
    		if(alarmTime == null) {
    			this.dataStorage.setData(CONF_ALARM_CLOCK_PROPERTY_NAME, DEFAULT_ALARM_TIME);
    			alarmTime = (Time) this.dataStorage.getData(CONF_ALARM_CLOCK_PROPERTY_NAME, Time.class);
    		}
    		
    		return Converter.convertTimeToString(alarmTime);
    	}catch(Exception e) {
    		return ErrorBuilder.buildError(formatStringException(e));
    	}
    }
    
    
    @GetMapping(value = "/setTimeAlarm")
    public String SetTimeAlarm(@RequestParam String date, @RequestParam(value = "token") String token) throws AuthenticationException {
    	String failedAuthMessage = processAuthorization(token);
    	if(failedAuthMessage != null)
    		return failedAuthMessage;
    	
    	try {
    		Time time = Converter.convertStringToTime(date);
    		this.dataStorage.setData(CONF_ALARM_CLOCK_PROPERTY_NAME, time);
    		
    		return "Success";
    	}catch(Exception e) {
    		return ErrorBuilder.buildError(formatStringException(e));
    	}
    }

    @GetMapping(value = "/getVolum")
    public String GetVolum(@RequestParam(value = "token") String token) throws AuthenticationException {
    	String failedAuthMessage = processAuthorization(token);
    	if(failedAuthMessage != null)
    		return failedAuthMessage;
    	
    	try {
    		Integer volum = (Integer) this.dataStorage.getData(CONF_VOLUM_PROPERTY_NAME, Integer.class);
    		if(volum == null) {
    			this.dataStorage.setData(CONF_VOLUM_PROPERTY_NAME, DEFAULT_VOLUM);
    			volum = (Integer) this.dataStorage.getData(CONF_VOLUM_PROPERTY_NAME, Integer.class);
    		}
    		
    		return volum.toString();
    	}catch(Exception e) {
    		return ErrorBuilder.buildError(formatStringException(e));
    	}
    }
    
    
    @GetMapping(value = "/setVolum")
    public String SetVolum(@RequestParam String levelVolum, @RequestParam(value = "token") String token) throws AuthenticationException {
    	String failedAuthMessage = processAuthorization(token);
    	if(failedAuthMessage != null)
    		return failedAuthMessage;
    	
    	try {
    		int volum = Converter.convertStringToVolum(levelVolum);
    		this.dataStorage.setData(CONF_VOLUM_PROPERTY_NAME, volum);
    		
    		return "Success";
    	}catch(Exception e) {
    		return ErrorBuilder.buildError(formatStringException(e));
    	}
    }
    
    
    @GetMapping(value = "/help", produces = {"application/json"})
    public String GetMethodsList(@RequestParam(value = "token") String token, HttpServletResponse response) {
    	
    	ArrayList<String> wsMethods = new ArrayList<>();
    	
    	Method [] methodsOfControllerClass = DriveController.class.getDeclaredMethods();
    	for (Method method : methodsOfControllerClass) {
    		GetMapping getAnnotation = method.getAnnotation(GetMapping.class);
    		PostMapping postAnnotation = method.getAnnotation(PostMapping.class);
    		RequestMapping requestMappingAnnotation = method.getAnnotation(RequestMapping.class);
    		
    		
    		String paramInfos = "";
    		for (Class<?> classParameters : method.getParameterTypes()) {
    			if(paramInfos.isEmpty())
    				paramInfos = classParameters.getSimpleName();
    			else
    				String.join(", ", paramInfos, classParameters.getSimpleName());
			}
    		
    		String methodDetails = null;    		
    		if(getAnnotation != null) {
    			methodDetails = String.format("\"[GET] - %s    -   (paramsTypes: %s)\"", getAnnotation.value()[0], paramInfos);
    		}else if(postAnnotation != null) {
    			methodDetails = String.format("\"[POST] - %s    -   (paramsTypes: %s)\"", postAnnotation.value()[0], paramInfos);
    		}else if(requestMappingAnnotation != null) {
    			String methodTypeName = requestMappingAnnotation.method()[0].name();
    			methodDetails = String.format("\"[%s] - %s    -   (paramsTypes: %s)\"", methodTypeName, requestMappingAnnotation.value()[0], paramInfos);
    		}
    		
    		if(methodDetails != null)
    			wsMethods.add(methodDetails);
		}
    			
    	for(int i = 0; i < wsMethods.size(); i++) {
    		wsMethods.set(i, "\"WS_Method n° "+i+"\": " + wsMethods.get(i));
    	}
    	
    	return "{" + String.join(",", wsMethods) + "}";
    }
    
    
    
    
    
    
    /*
     *  Private tools
     */
    

	private String formatStringException(Exception e) {
    	String stringStackTrace;
    	if(e.getCause() != null)
    		stringStackTrace = Converter.convertStacktraceToString(e.getCause().getStackTrace());
    	else
    		stringStackTrace = Converter.convertStacktraceToString(e.getStackTrace());
    	
    	return e.getMessage() + "       -----||-----     " + e.getClass().getName() + " ----- " + stringStackTrace;
    }
    
    private String processAuthorization(String token) throws AuthenticationException {
    	if(!serverTokenProvider.AuthorizeRequestToken(token)) {
    		return ErrorBuilder.buildError("The token does not equal the server-side token.");
    	}else {
    		return null;
    	}
    }
}
