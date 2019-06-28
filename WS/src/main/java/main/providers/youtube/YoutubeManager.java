package main.providers.youtube;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import main.model.youtube.playlist.PlaylistContentGroupInfo;
import main.providers.DataStorage;

public class YoutubeManager {
	
	@Autowired
	YoutubePlaylistComposer playlistComposer;
	
	@Autowired
	DataStorage dataStorage;
	
	private final int DEFAULT_FORMULA = YoutubePlaylistComposer.FORMULA_BASIC;
	private final String FORMULA_SELECTED_CONFIG_NAME = "wakeUp_formulaSelected";
	
	
	public YoutubeManager() {
		
	}
	
	
	//TODO The method who will call this one will be a WS method in the CommandController
	public void UpdateYoutubePlaylist() throws Exception {
		//TODO Make the playlist update
		
		dataStorage.getData(FORMULA_SELECTED_CONFIG_NAME, Integer.class);
		int codeFormula = 0; //TODO Get the code from the config file
				
		ArrayList<PlaylistContentGroupInfo> contentGroupInfo = this.playlistComposer.Compose(codeFormula);
		
		
		//Then we write here (may be dispatched in private methods) the logic to navigate on youtube (by youtube client) and update the playlist according to the contentGroupInfo datas 
	}
}
