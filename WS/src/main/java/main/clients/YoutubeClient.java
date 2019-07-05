package main.clients;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.Playlists.Delete;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemSnippet;
import com.google.api.services.youtube.model.PlaylistSnippet;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.Video;

import main.clients.requestparams.VideoDetails;
import main.clients.requestparams.VideoDuration;
import main.model.youtube.playlist.VideoItem;

@Service
public class YoutubeClient implements IYoutubeClient {

	private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

	private static final String APPLICATION_NAME = "RegularTasks-mediation";
//	private static final List<String> SCOPES = Collections.singletonList(TasksScopes.TASKS);
	private static final Set<String> SCOPES = YouTubeScopes.all();
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";

	private static final Long DEFAULT_VIDEO_RESEARCH_LIMIT_RESULT = Long.valueOf(30);
	private static final Long DEFAULT_PLAYLIST_LIMIT_RESULT = Long.valueOf(5);

	private YouTube youtubeService;
	

	public YoutubeClient() throws GeneralSecurityException, IOException {

		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		youtubeService = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
				.setApplicationName(APPLICATION_NAME).build();
	}
	
	// Not working on my single test (but was also not working on the google explorer)
	public String retrieveChannelID(String channelUsername) throws IOException {
		String part = "id"; // The output fields I want to fetch (the less possible is better because fields cost on the quota authorized)
		com.google.api.services.youtube.YouTube.Channels.List request = this.youtubeService.channels().list(part);
		
		ChannelListResponse response = request.setForUsername(channelUsername).execute();
		Channel channel = response.getItems().stream().findFirst().get();
		
		return channel.getId();
	}
	
	public String retrievePlaylistIDOfChannel(String playlistName, String channelId) throws Exception{
		String part = "id";
		com.google.api.services.youtube.YouTube.Search.List request = this.youtubeService.search().list(part);

		request.setChannelId(channelId).setType("playlist").setQ(playlistName);
		
		return request.execute().getItems().stream().findFirst().orElseThrow(
				() -> new Exception("No playlist has been retrieved when searching for a specific playlist"))
				.getId().getPlaylistId();
	}
	
	public String retrievePlaylistIDOfMyAccount(String playlistName) throws Exception {
		String part = "snippet";
		com.google.api.services.youtube.YouTube.Playlists.List request = this.youtubeService.playlists().list(part);

		request.setMine(true);
		
		Playlist myPlaylist = request.execute().getItems().stream().filter(item -> item.getSnippet().getTitle().equals(playlistName)).findFirst().orElse(null);
		return myPlaylist == null ? null : myPlaylist.getId();
	}
	
	/**
	 * 
	 * @param idPage
	 * @param videoDurationFilter (optional)
	 * @param resultLimit (optional)
	 * @return a list of the last videos in this channel
	 * @throws IOException 
	 */
	public ArrayList<VideoItem> listChannelVideos(String channelId, VideoDuration videoDurationFilter, Long resultLimit) throws IOException {
		String part = "snippet";
		ArrayList<VideoItem> result = new ArrayList<VideoItem>();
		com.google.api.services.youtube.YouTube.Search.List request = this.youtubeService.search().list(part);

		request.setChannelId(channelId);
		
		// Filtering
		request.setOrder("date").setType("video");
		if(resultLimit == null) {
			System.out.println(String.format("WARN: The default result limit is used for the search video listing (default limit = %l)", DEFAULT_PLAYLIST_LIMIT_RESULT));
			request.setMaxResults(DEFAULT_VIDEO_RESEARCH_LIMIT_RESULT);
		}else {
			request.setMaxResults(resultLimit);
		}
		if(videoDurationFilter != null)
			request.setVideoDuration(videoDurationFilter.getTextCode());
		
		// Request Execution and collecting results
		request.execute().getItems().forEach(item -> {
			String id = item.getId().getVideoId();
			String title = item.getSnippet().getTitle();
			Calendar creationDate = Calendar.getInstance();
			creationDate.setTimeInMillis(item.getSnippet().getPublishedAt().getValue());
			result.add(new VideoItem(id, title, creationDate));
		});
		
		return result;
	}
	
	/**
	 * It is not possible to get a lot of informations, or to control much the getting video request on a playlist.
	 * So get only the last N videos of a given playlist
	 * 
	 * @param playlistId
	 * @param videoDurationFilter
	 * @return
	 * @throws IOException 
	 */
	public ArrayList<VideoItem> listPlaylistVideos(String playlistId, Long resultLimit) throws IOException {
		String part = "snippet,contentDetails";
		ArrayList<VideoItem> result = new ArrayList<VideoItem>();
		
		
		com.google.api.services.youtube.YouTube.PlaylistItems.List request = this.youtubeService.playlistItems().list(part);
		
		request.setPlaylistId(playlistId);
				
		if(resultLimit == null) {
			System.out.println(String.format("WARN: The default result limit is used for the playlist video listing (default limit = %l)", DEFAULT_PLAYLIST_LIMIT_RESULT));
			request.setMaxResults(DEFAULT_PLAYLIST_LIMIT_RESULT);
		}else {
			request.setMaxResults(resultLimit);
		}
		
		request.execute().getItems().forEach(item -> {
			String id = item.getContentDetails().getVideoId();
			String title = item.getSnippet().getTitle();
			Calendar creationDate = Calendar.getInstance();
			creationDate.setTimeInMillis(item.getSnippet().getPublishedAt().getValue());
			result.add(new VideoItem(id, title, creationDate));
		});
		
		return result;
	}
	
	/**
	 * @return The ID of the new playlist
	 */
	public String CreatePlaylist(String playlistName) throws Exception {
		String part = "id,snippet";		
		Playlist playlist = new Playlist();
		playlist.setSnippet(new PlaylistSnippet().setTitle(playlistName));
		
        YouTube.Playlists.Insert request = this.youtubeService.playlists().insert(part, playlist);
        Playlist newPlaylist = request.execute();
        
        return newPlaylist.getId();
	}
	
	public void DeletePlaylist(String playlistId) throws Exception {
		Delete request = this.youtubeService.playlists().delete(playlistId);
		request.execute();
	}
	
	public void AddVideoToPlaylist(String videoId, String playlistId) throws Exception {
		String part = "snippet";
		PlaylistItem playlistItem = new PlaylistItem();
		playlistItem.setSnippet(new PlaylistItemSnippet().setPlaylistId(playlistId).setResourceId(new ResourceId().setVideoId(videoId).setKind("youtube#video")));
		
        YouTube.PlaylistItems.Insert request = youtubeService.playlistItems().insert(part, playlistItem);
        request.execute();
	}

	@Override
	public VideoDetails retrieveDetailsOfVideo(String videoId) throws Exception {
		String part = "snippet,contentDetails";
		
		com.google.api.services.youtube.YouTube.Videos.List request = this.youtubeService.videos().list(part);
		
		request.setId(videoId);

		request.setMaxResults(DEFAULT_PLAYLIST_LIMIT_RESULT);
		
		Video videoDetails = request.execute().getItems().get(0);
		
		
		return new VideoDetails(videoDetails.getContentDetails().getDuration(), videoDetails.getSnippet().getPublishedAt());
	}
	
	
	
	
	/*
	 * Private functions
	 */

	
	
	/**
	 * Creates an authorized Credential object.
	 * 
	 * @param HTTP_TRANSPORT The network HTTP Transport.
	 * @return An authorized Credential object.
	 * @throws IOException If the credentials.json file cannot be found.
	 */
	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		// Load client secrets.
		InputStream in = YoutubeClient.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		if (in == null) {
			String errorMessage = "Please add the '" + CREDENTIALS_FILE_PATH
					+ "' file to the resource folder (src/main/resources)";
			throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH + ". " + errorMessage);
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES)
						.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
						.setAccessType("offline").build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}

}
