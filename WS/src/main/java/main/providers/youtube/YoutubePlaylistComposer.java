package main.providers.youtube;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import main.model.youtube.playlist.PlaylistContentGroupInfo;

/**
 *  The composer to get the information of how to compose a playlist based on a predefined formula code.
 * 
 * @author rbenhmidane
 *
 */
@Component
public class YoutubePlaylistComposer {

	public static final int FORMULA_BASIC = 1; // Composed of : ten minutes of music trends, then 10 minutes of news, then ten minutes of rocket league videos 
	
	public ArrayList<PlaylistContentGroupInfo> Compose(int codeFormula) throws Exception {
		ArrayList<PlaylistContentGroupInfo> result;
		
		switch (codeFormula) {
			case FORMULA_BASIC:
				//TODO To code
				result = null;
				break;
			default:
				throw new Exception(String.format("No formula with that ID exists (ID = %d)", codeFormula));
		}
		
		return result;
	}
}
