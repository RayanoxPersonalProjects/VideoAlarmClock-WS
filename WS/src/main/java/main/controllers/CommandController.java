package main.controllers;

import java.util.Calendar;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import main.controllers.common.AbstractController;
import main.model.CommandSequence;
import main.model.CommandSequenceBuilder;
import main.model.ContentType;
import main.model.ErrorBuilder;
import main.providers.ICommandProvider;
import main.providers.InfoChannel;

@RestController
public class CommandController extends AbstractController{

	public static final Integer MAX_WAKEUP_DURATION_MINUTES = 40;
	
	@Autowired
	ICommandProvider commandProvider;

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
    		CommandSequence contentCommands;
    		
    		// Compute the content commands
    		switch(contentType) {
    			case INFO_CHANNEL :
					int dayOfYear = currentDate.get(Calendar.DAY_OF_YEAR);
					InfoChannel channelSelected = dayOfYear  % 2 == 0 ? InfoChannel.France24 : InfoChannel.euronews_playlist;
					contentCommands = commandProvider.GetContentCommand_InfoChannel(channelSelected);
    				result += String.format("[channelSelected = %s , DAY_O_Y = %s, command length = %d] - ", channelSelected.toString(), dayOfYear, ((commands.length()+1)/2) - 2);
    				break;
    			case CUSTOM_DAILY_YOUTUBE_PLAYLIST:
    				
    				contentCommands = commandProvider.GetContentCommand_CustomDailyYoutubePlaylist();
    				break;
    			default:
    				result += ErrorBuilder.buildError("The content type requested by the parameter contentTypeCode in the 'GetCommand' WS method does not exist in the system. Please request an other operation type");
    				return result;
    		}
    		
    		// Compute all the commands
    		commands = joinCommands(commands, this.commandProvider.GetCommands_ForTimerPeriod());
    		commands = joinCommands(commands, this.commandProvider.GetCommands_ForWakingUp(contentCommands));
    		commands = joinCommands(commands, this.commandProvider.GetCommands_ForClosing(MAX_WAKEUP_DURATION_MINUTES));
    		
    		return result + setEnclosingCharacters(commands);
    	}catch(Exception e) {
    		return result + ErrorBuilder.buildError(formatStringException(e));
    	}
    }

    
    
    /*
     *  Private tools
     */
    
	private String setEnclosingCharacters(String commands) {
		return String.format("{%s}", commands);
	}
	
	private String joinCommands(String firstCommand, String secondCommand) {
		return firstCommand + CommandSequenceBuilder.delimiter + secondCommand;
	}
    
}
