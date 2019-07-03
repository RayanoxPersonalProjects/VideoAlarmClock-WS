package main.providers.youtube;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import main.clients.IYoutubeClient;
import main.clients.requestparams.VideoDuration;
import main.exceptions.NotImplementedException;
import main.model.youtube.playlist.PlaylistContentGroupInfo;
import main.model.youtube.playlist.PlaylistVideoType;
import main.model.youtube.playlist.RetrievingSequenceType;
import main.model.youtube.playlist.VideoItem;
import main.providers.DataStorage;

public class YoutubeManager {
	
	@Autowired
	YoutubePlaylistComposer playlistComposer;
	
	@Autowired
	DataStorage dataStorage;
	
	@Autowired
	IYoutubeClient youtubeClient;
	
	private final int DEFAULT_FORMULA = YoutubePlaylistComposer.FORMULA_BASIC;
	private final String FORMULA_SELECTED_CONFIG_NAME = "youtubePlaylist_formulaSelected";
	
	private static final String CUSTOM_PLAYLIST_NAME = "CustomWakeUpPlaylist";
	
	
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
		
		//TODO A continuer (utiliser un thread pour faire le boulot d'ajout à un moment donné) -> A réfléchir car si le traitement prends peu de temps, ça ne vaut surement pas le coup d'utiliser un thread et mieux vaut alors attendre de connaitre le résultat du traitement.
		
		// Obtenir la séquence de vidéos à intégrer dans la playlist
		ArrayList<PlaylistContentGroupInfo> contentGroupInfo = this.playlistComposer.compose(formulaCode);
		
		// Retrieve videos
		ArrayList<VideoItem> videosToAdd = new ArrayList<VideoItem>();
		for (PlaylistContentGroupInfo groupInfo : contentGroupInfo) {
			videosToAdd.addAll(processSequenceVideoRetrievement(groupInfo));
		}

		// Clean my custom playlist
		String myCustomPlaylistId = this.youtubeClient.retrievePlaylistIDOfMyAccount(CUSTOM_PLAYLIST_NAME);
		this.youtubeClient.DeletePlaylist(myCustomPlaylistId);
		this.youtubeClient.CreatePlaylist(myCustomPlaylistId);
		
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
		
		VideoDuration videoDuration = VideoDuration.getVideoDurationForMaxMinutesValue(videoSeqInfos.getMaxMinutesForEachVideo());
		
		ArrayList<VideoItem> result = null;
		String channelId = null;
		switch(videoSequenceTypeInfo) {
			case CHANNEL_LASTVIDEOS:
				channelId = this.youtubeClient.retrieveChannelID(videoTypeInfo.getChannelName());
				result = this.youtubeClient.listChannelVideos(channelId, videoDuration, Long.valueOf(7));
				result = selectFilterVideos(result, videoSeqInfos, videoDuration);
				break;
			case MUSIC_TRENDS: // Not possible to access the real music trends through the API, so we use a playlist management
			case CHANNEL_PLAYLIST:
				channelId = this.youtubeClient.retrieveChannelID(videoTypeInfo.getChannelName());
				String playlistId = this.youtubeClient.retrievePlaylistIDOfChannel(videoTypeInfo.getPlaylistName(), channelId);
				result = this.youtubeClient.listChannelVideos(playlistId, videoDuration, Long.valueOf(7));
				result = selectFilterVideos(result, videoSeqInfos, videoDuration);
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

	// We don't have the duration information of the video in the research output of youtube API, so we use an alternative solution
	private ArrayList<VideoItem> selectFilterVideos(ArrayList<VideoItem> videoList, PlaylistContentGroupInfo videoSeqInfos, VideoDuration videoDuration) {
		int maxGroupDuration = videoSeqInfos.getMaxMinutesForGroup();
		
		ArrayList<VideoItem> result = new ArrayList<VideoItem>();
		
		int currentVideoDuration = 0;
		
		for (VideoItem video : videoList) {
			currentVideoDuration += videoDuration.getLessThanMinutes();
			if(currentVideoDuration <= maxGroupDuration)
				result.add(video);
		}
		
		return result;
	}
	
}
