package main.model.youtube.playlist;

public enum PlaylistVideoType {
	
	// News
	News_Euronews("euronews", null, null, RetrievingSequenceType.CHANNEL_LASTVIDEOS),
	
	// Musics
	NewMusics("#RedMusic: FavoriteHits", "UCon7aStbKMlm_3VWwHkNFBg", "Top 50 Nouveautés 2019: Musique du Moment", RetrievingSequenceType.MUSIC_TRENDS),
	
	// Entertainments
	Entertainment_RocketLeague_Squishy("SquishyMuffinz", null, null, RetrievingSequenceType.CHANNEL_LASTVIDEOS);
	
	private String channelId;
	private String channelName;
	private String playlistName;
	private RetrievingSequenceType retrievingSequenceInfo; 
	
	
	private PlaylistVideoType(String channelName, String channelId, String playlistName, RetrievingSequenceType retrievingSequenceInfo) {
		this.channelName = channelName;
		this.retrievingSequenceInfo = retrievingSequenceInfo;
		this.playlistName = playlistName;
		this.channelId = channelId;
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
	public String getChannelId() {
		return channelId;
	}
}
