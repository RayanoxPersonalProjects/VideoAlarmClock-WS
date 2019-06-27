package main.controllers;

import java.sql.Time;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import main.controllers.common.AbstractController;
import main.model.ErrorBuilder;
import main.providers.DataStorage;
import main.utils.Converter;

@RestController
public class InfosController extends AbstractController{

	private static final String CONF_VOLUM_PROPERTY_NAME = "volumLevel";
	private final Integer DEFAULT_VOLUM = 30;
	
	private final String CONF_ALARM_CLOCK_PROPERTY_NAME = "alarmClockTime";
	private final Time DEFAULT_ALARM_TIME = new Time(7, 30, 0);
	
	@Autowired
	private DataStorage dataStorage;
	

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
	
}
