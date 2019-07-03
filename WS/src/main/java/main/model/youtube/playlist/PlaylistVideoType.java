package main.model.youtube.playlist;

public enum PlaylistVideoType {
	
	// News
	News_Euronews("euronews", null, RetrievingSequenceType.CHANNEL_LASTVIDEOS),
	
	// Musics
	NewMusics("#RedMusic: FavoriteHits", "Top 50 Nouveautés 2019: Musique du Moment", RetrievingSequenceType.MUSIC_TRENDS),
	
	// Entertainments
	Entertainment_RocketLeague_Squishy("", null, RetrievingSequenceType.CHANNEL_LASTVIDEOS);
	
	private String channelName;
	private String playlistName;
	private RetrievingSequenceType retrievingSequenceInfo; 
	
	private PlaylistVideoType(String channelName, String playlistName, RetrievingSequenceType retrievingSequenceInfo) {
		this.channelName = channelName;
		this.retrievingSequenceInfo = retrievingSequenceInfo;
		this.playlistName = playlistName;
	}
	
	
	/*
	 *  Getters
	 */
	
	/**
	 * 
	 * @return the String to type in the research
	 */
	public String getChannelName() {
		return channelName;
	}
	public String getPlaylistName() {
		return playlistName;
	}
	public RetrievingSequenceType getRetrievingSequenceInfo() {
		return retrievingSequenceInfo;
	}
}
