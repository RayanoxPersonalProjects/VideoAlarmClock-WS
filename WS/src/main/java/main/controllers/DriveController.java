package main.controllers;

import java.util.ArrayList;
import java.util.Calendar;

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
import main.clients.IYoutubeClient;
import main.model.ContentType;
import main.model.ErrorBuilder;
import main.providers.IDataProvider;
import main.providers.InfoChannel;
import main.utils.Converter;

@RestController
public class DriveController {

	@Autowired
	ServerTokenProvider serverTokenProvider;
	
	@Autowired
	IDataProvider dataProvider;

    @RequestMapping(value = "/getCommands", method = RequestMethod.GET)
    public String GetCommands(@RequestParam(value = "token") String token, @RequestParam(value = "contentType") char contentTypeCode) throws AuthenticationException {
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
    		//allTasksOfFutureDays = googleTaskClient.GetAllRegularTasksOfFutureDays();
    	}catch(Exception e) {
    		return ErrorBuilder.buildError(formatStringException(e));
    	}
    	
    	
    	return "default response of getTimeAlarm method (not implemented yet)";
        //return new AllTasksResults(allTasksOfFutureDays);
    }
    
    
    @PostMapping(value = "/setTimeAlarm")
    public String SetTimeAlarm(@RequestBody String date, @RequestParam(value = "token") String token) throws AuthenticationException {
    	String failedAuthMessage = processAuthorization(token);
    	if(failedAuthMessage != null)
    		return failedAuthMessage;
    	   
//    	if(task == null || task.getId() == null || task.getName() == null || task.isCompleted() == null) {
//    		return getFailedOperationResult("At least one input argument is null. Retry without null arguments. Task content = \n" + task != null ? task.toString() : "null");
//    	}
    	
		//OperationResult resultOp = googleTaskClient.UpdateTask(task);
    	//String resultOp = dataProvider.UpdateTask(task);
    	
    	//return resultOp;
    	return "default response of setTimeAlarm method (not implemented yet)";
    }
    
    
    
    
    
    
    
    
    /*
     *  Private tools
     */
    

    private String formatStringException(Exception e) {
    	return e.getMessage() + "       -----||-----     " + e.getClass().getName() + " ----- " + Converter.convertStacktraceToString(e.getStackTrace());
    }
    
    private String processAuthorization(String token) throws AuthenticationException {
    	if(!serverTokenProvider.AuthorizeRequestToken(token)) {
    		return ErrorBuilder.buildError("The token does not equal the server-side token.");
    	}else {
    		return null;
    	}
    }
}
