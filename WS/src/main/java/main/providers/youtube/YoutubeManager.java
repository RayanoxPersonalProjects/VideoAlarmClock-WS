package main.providers.youtube;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import main.clients.IYoutubeClient;
import main.clients.requestparams.VideoDetails;
import main.clients.requestparams.VideoDuration;
import main.exceptions.NotImplementedException;
import main.model.youtube.playlist.PlaylistContentGroupInfo;
import main.model.youtube.playlist.PlaylistVideoType;
import main.model.youtube.playlist.RetrievingSequenceType;
import main.model.youtube.playlist.VideoItem;
import main.providers.DataStorage;
import main.utils.Converter;

@Component
public class YoutubeManager {
	
	@Autowired
	YoutubePlaylistComposer playlistComposer;
	
	@Autowired
	DataStorage dataStorage;
	
	@Autowired
	IYoutubeClient youtubeClient;
	
	private final int DEFAULT_FORMULA = YoutubePlaylistComposer.FORMULA_BASIC;
	private final String FORMULA_SELECTED_CONFIG_NAME = "youtubePlaylist_formulaSelected";
	
	private static final String CUSTOM_PLAYLIST_NAME = "Custom WakeUp Playlist";
	
	private final int RETRIEVE_VIDEO_COUNT_LIMIT = 30;
//	private final int RETRIEVE_VIDEO_COUNT_LIMIT = 7; // For tests purposes, I can use this line instead of the other one.
	
	
	public YoutubeManager() {
		
	}

	public void UpdateYoutubePlaylist() throws Exception {
		Integer formulaCode = (Integer) dataStorage.getData(FORMULA_SELECTED_CONFIG_NAME, Integer.class);

		if(formulaCode == null) {
			dataStorage.setData(FORMULA_SELECTED_CONFIG_NAME, DEFAULT_FORMULA);
			formulaCode = DEFAULT_FORMULA;
		}
		
		UpdateYoutubePlaylist(formulaCode); 
	}
	
	public void UpdateYoutubePlaylist(Integer formulaCode) throws Exception {
		if(!YoutubePlaylistComposer.getFormulasCodesMapping().values().contains(formulaCode))
			throw new Exception("No formula has a code that equals " + formulaCode);
		
		// Obtenir la séquence de vidéos à intégrer dans la playlist
		ArrayList<PlaylistContentGroupInfo> contentGroupInfo = this.playlistComposer.compose(formulaCode);
		
		// Retrieve videos
		ArrayList<VideoItem> videosToAdd = new ArrayList<VideoItem>();
		for (PlaylistContentGroupInfo groupInfo : contentGroupInfo) {
			videosToAdd.addAll(processSequenceVideoRetrievement(groupInfo));
		}

		// Clean my custom playlist
		String myCustomPlaylistId = this.youtubeClient.retrievePlaylistIDOfMyAccount(CUSTOM_PLAYLIST_NAME);
		if(myCustomPlaylistId != null)
			this.youtubeClient.DeletePlaylist(myCustomPlaylistId);
		myCustomPlaylistId = this.youtubeClient.CreatePlaylist(CUSTOM_PLAYLIST_NAME);
		
		// Add videos to playlist
		for (VideoItem video : videosToAdd) {
			this.youtubeClient.AddVideoToPlaylist(video.getId(), myCustomPlaylistId);
		}
	}
	
	
	/*
	 *  Private functions
	 */
	

	
	private ArrayList<VideoItem> processSequenceVideoRetrievement(PlaylistContentGroupInfo videoSeqInfos) throws Exception {
		PlaylistVideoType videoTypeInfo = videoSeqInfos.getTypeVideo();
		RetrievingSequenceType videoSequenceTypeInfo = videoTypeInfo.getRetrievingSequenceInfo();
		
		VideoDuration videoDuration = videoSeqInfos.isTypeSearchAny() ? 
				VideoDuration.Any 
				: VideoDuration.getVideoDurationForMaxMinutesValue(videoSeqInfos.getMaxMinutesForEachVideo());
		
		ArrayList<VideoItem> result = null;
		String channelId = videoTypeInfo.getChannelId() != null ? videoTypeInfo.getChannelId() : null;
		switch(videoSequenceTypeInfo) {
			case CHANNEL_LASTVIDEOS:
				if(channelId == null)
					channelId = this.youtubeClient.retrieveChannelID(videoTypeInfo.getChannelName());
				result = this.youtubeClient.listChannelVideos(channelId, videoDuration, Long.valueOf(RETRIEVE_VIDEO_COUNT_LIMIT));
				fillMissingInformations(result);
				result = selectFilterVideos(result, videoSeqInfos);
				break;
			case MUSIC_TRENDS: // Not possible to access the real music trends through the API, so we use a playlist management
			case CHANNEL_PLAYLIST:
				if(channelId == null)
					channelId = this.youtubeClient.retrieveChannelID(videoTypeInfo.getChannelName());
				String playlistId = this.youtubeClient.retrievePlaylistIDOfChannel(videoTypeInfo.getPlaylistName(), channelId);
				result = this.youtubeClient.listPlaylistVideos(playlistId, Long.valueOf(RETRIEVE_VIDEO_COUNT_LIMIT));
				fillMissingInformations(result);
				result = selectFilterVideos(result, videoSeqInfos);
				break;
			case SIMPLE_RESEARCH:
			default:
				throw new NotImplementedException(String.format("The sequence video retrievement has not been implemented for the %s type", videoSequenceTypeInfo));
		}
		
		if(result == null) {
			result = new ArrayList<VideoItem>();
			System.err.println("An empty video array is generated (no video retrieved from the youtube research ?)");
		}
		
		return result;
	}


	private void fillMissingInformations(ArrayList<VideoItem> videoList) throws Exception {
		for (VideoItem video : videoList) {
			if(video.getDuration() == null || video.getCreationDate() == null) {
				VideoDetails videoDetails = this.youtubeClient.retrieveDetailsOfVideo(video.getId());

				if(videoDetails.videoDuration.isZero()) // A duration of Zero is a live video
					continue;
				
				if(video.getDuration() == null)
					video.setDuration(videoDetails.videoDuration);
				if(video.getCreationDate() == null)
					video.setCreationDate(videoDetails.creationDate);
			}
		}
	}

	// We don't have the duration information of the video in the research output of youtube API, so we use an alternative solution
	private ArrayList<VideoItem> selectFilterVideos(ArrayList<VideoItem> videoList, PlaylistContentGroupInfo videoSeqInfos) {
		int maxGroupDuration = videoSeqInfos.getMaxMinutesForGroup();
		
		ArrayList<VideoItem> result = new ArrayList<VideoItem>();
		
		double totalVideoDuration = 0;// In minutes
		
		for (VideoItem video : videoList) {
			double currentVideoDuration = Converter.convertSecondsToMinutes(video.getDuration().getSeconds());
			
			if(totalVideoDuration + currentVideoDuration <= maxGroupDuration) {
				result.add(video);
				totalVideoDuration += Converter.convertSecondsToMinutes(video.getDuration().getSeconds());
			}
		}
		
		return result;
	}
	
}
