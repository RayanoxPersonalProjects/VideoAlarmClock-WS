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

	private final String CONF_VOLUM_PROPERTY_NAME = "volumLevel";
	private final Integer DEFAULT_VOLUM = 30;
	
	private final String CONF_ALARM_CLOCK_PROPERTY_NAME = "alarmClockTime";
	private final Time DEFAULT_ALARM_TIME = new Time(7, 30, 0);
	
	private final String CONF_SHUTDOWN_TIMER_VALUE_PROPERTY_NAME = "shutdownTimerValue";
	private final Integer DEFAULT_SHUTDOWN_TIMER_VALUE = 40; // In minutes
	
	
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
    public String SetTimeAlarm(@RequestParam String timeParam, @RequestParam(value = "token") String token) throws AuthenticationException {
    	String failedAuthMessage = processAuthorization(token);
    	if(failedAuthMessage != null)
    		return failedAuthMessage;
    	
    	try {
    		Time time = Converter.convertStringToTime(timeParam);
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
    
    
    
    /**
     * Timer used to know the time to close the TV and devices before sleeping (time to start after Arduino activation)
     * 
     * @param token
     * @return the value of the timer in minutes
     * @throws AuthenticationException
     */
    @GetMapping(value = "/getShutdownTimer")
    public String GetShutdownTimer(@RequestParam(value = "token") String token) throws AuthenticationException {
    	String failedAuthMessage = processAuthorization(token);
    	if(failedAuthMessage != null)
    		return failedAuthMessage;
    	
    	try {
    		Integer minutesTimer = (Integer) this.dataStorage.getData(CONF_SHUTDOWN_TIMER_VALUE_PROPERTY_NAME, Integer.class);
    		if(minutesTimer == null) {
    			this.dataStorage.setData(CONF_SHUTDOWN_TIMER_VALUE_PROPERTY_NAME, DEFAULT_SHUTDOWN_TIMER_VALUE);
    			minutesTimer = (Integer) this.dataStorage.getData(CONF_SHUTDOWN_TIMER_VALUE_PROPERTY_NAME, Integer.class);
    		}
    		
    		return minutesTimer.toString();
    	}catch(Exception e) {
    		return ErrorBuilder.buildError(formatStringException(e));
    	}
    }
    
    
    /**
     * Timer used to know the time to close the TV and devices before sleeping (time to start after Arduino activation)
     * 
     * @param minutesTimer (Integer in minutes)
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @GetMapping(value = "/setShutdownTimer")
    public String SetShutdownTimer(@RequestParam String minutesTimer, @RequestParam(value = "token") String token) throws AuthenticationException {
    	String failedAuthMessage = processAuthorization(token);
    	if(failedAuthMessage != null)
    		return failedAuthMessage;
    	
    	try {
    		Integer time = Integer.valueOf(minutesTimer);
    		this.dataStorage.setData(CONF_SHUTDOWN_TIMER_VALUE_PROPERTY_NAME, time);
    		
    		return "Success";
    	}catch(Exception e) {
    		return ErrorBuilder.buildError(formatStringException(e));
    	}
    }
    
    
}
