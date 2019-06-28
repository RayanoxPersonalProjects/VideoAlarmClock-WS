package main.model.youtube.playlist;

/**
 *  This info is the sequence used to get the videos on youtube. Each enum value is composed of the sequence of video access separated by a '_'.
 * 
 * @author rbenhmidane
 *
 */
public enum RetrievingSequenceType {
	
	CHANNEL_PLAYLIST,
	CHANNEL_LASTVIDEOS, // The most used
	SIMPLE_RESEARCH,
	MUSIC_TRENDS; // Nouvelles tendances musicales
	
}
