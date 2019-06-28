package main.providers;

import java.util.Collection;

import main.model.PsButton;

public interface ICommandProvider {
	
	String GetContentCommand_SimpleVideo() throws Exception;
	String GetContentCommand_InfoChannel(InfoChannel channel) throws Exception;
	String GetContentCommand_CustomDailyYoutubePlaylist() throws Exception;
}
