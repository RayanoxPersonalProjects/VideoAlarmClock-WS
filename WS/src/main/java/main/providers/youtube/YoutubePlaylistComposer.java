package main.providers.youtube;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Component;

import main.model.youtube.playlist.PlaylistContentGroupInfo;
import main.model.youtube.playlist.PlaylistVideoType;

/**
 *  The composer to get the information of how to compose a playlist based on a predefined formula code.
 * 
 * @author rbenhmidane
 *
 */
@Component
public class YoutubePlaylistComposer {

	public static final int FORMULA_BASIC = 1; // Composed of : ten minutes of music trends, then 10 minutes of news, then ten minutes of rocket league videos 
	
	private static HashMap<String, Integer> mapFormulas;
		
	public ArrayList<PlaylistContentGroupInfo> compose(int codeFormula) throws Exception {
		ArrayList<PlaylistContentGroupInfo> result = new ArrayList<PlaylistContentGroupInfo>();
		
		switch (codeFormula) {
			case FORMULA_BASIC:
				
				int maxMinutesForEachVideo = 4;
				int maxMinutesForGroup = 15;
				
				result.add(new PlaylistContentGroupInfo(PlaylistVideoType.NewMusics, maxMinutesForEachVideo, maxMinutesForGroup));
				result.add(new PlaylistContentGroupInfo(PlaylistVideoType.News_Euronews, maxMinutesForEachVideo, maxMinutesForGroup));
				result.add(new PlaylistContentGroupInfo(PlaylistVideoType.Entertainment_RocketLeague_Squishy, maxMinutesForGroup, maxMinutesForGroup));
				
				break;
			default:
				throw new Exception(String.format("No formula with that ID exists (ID = %d)", codeFormula));
		}
		
		return result;
	}
	
	
	
	

	
	public static HashMap<String, Integer> getFormulasCodesMapping() throws IllegalArgumentException, IllegalAccessException {
		if(mapFormulas == null) {
			HashMap<String, Integer> mapResult = new HashMap<String, Integer>();
			
			Class<?> ter = YoutubePlaylistComposer.class;
	    	Field [] fields = ter.getFields();
	    	
	    	for (Field field : fields) {
				String variableName = field.getName();
				
				if(variableName.startsWith("FORMULA_" )) {
					int variableValue = field.getInt(field);
					mapResult.put(variableName, variableValue);
				}
			}
	    	
	    	return mapResult;
		}else {
			return mapFormulas;
		}
	}
}
