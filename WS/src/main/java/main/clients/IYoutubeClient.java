package main.clients;

import java.io.IOException;
import java.util.ArrayList;

import main.clients.requestparams.VideoDuration;
import main.model.youtube.playlist.VideoItem;

public interface IYoutubeClient {
	
	String retrieveChannelID(String channelUsername) throws IOException;
	
	String retrievePlaylistIDOfChannel(String playlistName, String channelID) throws Exception;
	
	String retrievePlaylistIDOfMyAccount(String playlistName) throws Exception;
	
	ArrayList<VideoItem> listChannelVideos(String channelId, VideoDuration videoDurationFilter, Long resultLimit) throws IOException;
	
	ArrayList<VideoItem> listPlaylistVideos(String playlistId, VideoDuration videoDurationFilter, Long resultLimit) throws IOException;
	
	void CreatePlaylist(String playlistName) throws Exception;
	
	void DeletePlaylist(String playlistId) throws Exception;
	
	void AddVideoToPlaylist(String videoId, String playlistId) throws Exception;
}
