package main.controllers;

import java.sql.Time;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import main.controllers.common.AbstractController;
import main.model.ErrorBuilder;
import main.providers.youtube.YoutubeManager;
import main.providers.youtube.YoutubePlaylistComposer;
import main.utils.Converter;

@RestController
public class OperationController extends AbstractController{
	
	@Autowired
	private YoutubeManager youtubeManager;
	
	@GetMapping(value = "/UpdatePlaylist")
	public String UpdateCustomYoutubePlaylist(@RequestParam String token) throws AuthenticationException {
		String failedAuthMessage = processAuthorization(token);
    	if(failedAuthMessage != null)
    		return failedAuthMessage;
		
    	try {
			this.youtubeManager.UpdateYoutubePlaylist();
			return "Success";
			
    	}catch(Exception e) {
    		return ErrorBuilder.buildError(formatStringException(e));
    	}
	}
	
	@GetMapping(value = "/UpdatePlaylist", params = "formulaCode")
	public String UpdateCustomYoutubePlaylist(@RequestParam String token, @RequestParam Integer formulaCode) throws AuthenticationException {
		String failedAuthMessage = processAuthorization(token);
    	if(failedAuthMessage != null)
    		return failedAuthMessage;
    	
    	try {
    		
    		this.youtubeManager.UpdateYoutubePlaylist(formulaCode);
    		
    		return "Success";
    	}catch(Exception e) {
    		return ErrorBuilder.buildError(formatStringException(e));
    	}
	}
	
}
