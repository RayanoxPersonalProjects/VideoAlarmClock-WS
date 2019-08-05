package main.controllers;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import main.controllers.common.AbstractController;
import main.model.ErrorBuilder;
import main.providers.youtube.YoutubeManager;

@RestController
public class OperationController extends AbstractController{
	
	public static final String UPDATE_PLAYLIST_PATH = "/UpdatePlaylist";
	
	@Autowired
	private YoutubeManager youtubeManager;
	
	@GetMapping(value = UPDATE_PLAYLIST_PATH)
	public String UpdateCustomYoutubePlaylist(@RequestParam String token) throws AuthenticationException {
		String failedAuthMessage = processAuthorization(token);
    	if(failedAuthMessage != null)
    		return failedAuthMessage;
		
    	try {
			this.youtubeManager.UpdateYoutubePlaylist(null);
			return "Success";
			
    	}catch(Exception e) {
    		return ErrorBuilder.buildError(formatStringException(e));
    	}
	}
	
	@GetMapping(value = UPDATE_PLAYLIST_PATH, params = "formulaCode")
	public String UpdateCustomYoutubePlaylist(@RequestParam String token, @RequestParam Integer formulaCode) throws AuthenticationException {
		String failedAuthMessage = processAuthorization(token);
    	if(failedAuthMessage != null)
    		return failedAuthMessage;
    	
    	try {
    		
    		this.youtubeManager.UpdateYoutubePlaylist(formulaCode, null);
    		
    		return "Success";
    	}catch(Exception e) {
    		return ErrorBuilder.buildError(formatStringException(e));
    	}
	}
	
}
