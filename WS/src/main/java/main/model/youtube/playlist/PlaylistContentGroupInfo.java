package main.model.youtube.playlist;

import main.clients.requestparams.VideoDuration;

public class PlaylistContentGroupInfo {
	private PlaylistVideoType typeVideo;
	private int maxMinutesForEachVideo;
	private int maxMinutesForGroup;
	
	private boolean typeSearchAny = false;
	
	/**
	 * 
	 * @param typeVideo
	 * @param maxMinutesForEachVideo
	 * @param maxMinutesForGroup
	 * @param typeSearchAny If true, the search on youtube not filter by video size (ANY type)
	 */
	public PlaylistContentGroupInfo(PlaylistVideoType typeVideo, int maxMinutesForEachVideo, int maxMinutesForGroup, boolean typeSearchAny) {
		this.typeVideo = typeVideo; 
		this.maxMinutesForEachVideo = maxMinutesForEachVideo;
		this.maxMinutesForGroup = maxMinutesForGroup;
		this.typeSearchAny = typeSearchAny;
	}
	
	public PlaylistContentGroupInfo(PlaylistVideoType typeVideo, int maxMinutesForEachVideo, int maxMinutesForGroup) {
		this(typeVideo, maxMinutesForEachVideo, maxMinutesForGroup, false);
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
	public boolean isTypeSearchAny() {
		return typeSearchAny;
	}
}
