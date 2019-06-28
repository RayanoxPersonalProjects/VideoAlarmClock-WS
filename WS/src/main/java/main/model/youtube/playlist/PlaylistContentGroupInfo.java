package main.model.youtube.playlist;

public class PlaylistContentGroupInfo {
	private PlaylistVideoType typeVideo;
	private int maxMinutesForEachVideo;
	private int maxMinutesForGroup;
	
	public PlaylistContentGroupInfo(PlaylistVideoType typeVideo, int maxMinutesForEachVideo, int maxMinutesForGroup) {
		this.typeVideo = typeVideo; 
		this.maxMinutesForEachVideo = maxMinutesForEachVideo;
		this.maxMinutesForGroup = maxMinutesForGroup;
	}
	
	
	
	
	/*
	 *  Getters / Setters
	 */
	
	public int getMaxMinutesForEachVideo() {
		return maxMinutesForEachVideo;
	}
	public int getMaxMinutesForGroup() {
		return maxMinutesForGroup;
	}
	public PlaylistVideoType getTypeVideo() {
		return typeVideo;
	}
}
