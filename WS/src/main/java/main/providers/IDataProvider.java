package main.providers;

public interface IDataProvider {
	
	String [] GetContentCommand_SimpleVideo();
	String [] GetContentCommand_InfoChannel();
	String [] GetContentCommand_CustomDailyYoutubePlaylist();
}
