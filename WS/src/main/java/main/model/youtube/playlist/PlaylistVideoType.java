package main.model.youtube.playlist;

public enum PlaylistVideoType {
	
	// News
	News_Euronews("euronews", RetrievingSequenceType.CHANNEL_LASTVIDEOS),
	
	// Musics
	NewMusics("", RetrievingSequenceType.MUSIC_TRENDS),
	
	// Entertainments
	Entertainment_RocketLeague_Squishy("", RetrievingSequenceType.CHANNEL_LASTVIDEOS);
	
	private String source;
	private RetrievingSequenceType retrievingSequenceInfo; 
	
	private PlaylistVideoType(String source, RetrievingSequenceType retrievingSequenceInfo) {
		this.source = source;
		this.retrievingSequenceInfo = retrievingSequenceInfo;
	}
	
	
	/*
	 *  Getters
	 */
	
	public String getSource() {
		return source;
	}
	public RetrievingSequenceType getRetrievingSequenceInfo() {
		return retrievingSequenceInfo;
	}
}
