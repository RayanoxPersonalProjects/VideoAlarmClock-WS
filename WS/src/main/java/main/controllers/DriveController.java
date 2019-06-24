package main.controllers;

import java.util.ArrayList;

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
import main.model.ErrorBuilder;
import main.providers.IDataProvider;
import main.utils.Converter;

@RestController
public class DriveController {

	@Autowired
	ServerTokenProvider serverTokenProvider;
	
	@Autowired
	IDataProvider dataProvider;

    @RequestMapping(value = "/getCommands", method = RequestMethod.GET)
    public String GetCommands(@RequestParam(value = "token") String token) throws AuthenticationException {
    	String failedAuthMessage = processAuthorization(token);
    	if(failedAuthMessage != null)
    		return failedAuthMessage;
    	
    	//ArrayList<Task> allTasksOfCurrentDay;
    	
    	try {
    		//allTasksOfCurrentDay = googleTaskClient.GetAllRegularTasksOfCurrentDay();
    	}catch(Exception e) {
    		return ErrorBuilder.buildError(formatStringException(e));
    	}
    	
    	return "default response of getCommand method (not implemented yet)";
        //return new AllTasksResults(allTasksOfCurrentDay);
    }
    
    @GetMapping(value = "/getTimeAlarm")
    public String GetTimeAlarm(@RequestParam(value = "token") String token) throws AuthenticationException {
    	String failedAuthMessage = processAuthorization(token);
    	if(failedAuthMessage != null)
    		return failedAuthMessage;
    	
    	//ArrayList<Task> allTasksOfFutureDays;
    	
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
