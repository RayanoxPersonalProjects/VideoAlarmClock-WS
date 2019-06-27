package main.controllers;

import java.util.Calendar;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import main.controllers.common.AbstractController;
import main.model.ContentType;
import main.model.ErrorBuilder;
import main.providers.IDataProvider;
import main.providers.InfoChannel;

@RestController
public class CommandController extends AbstractController{

	@Autowired
	IDataProvider dataProvider;

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
    
}
