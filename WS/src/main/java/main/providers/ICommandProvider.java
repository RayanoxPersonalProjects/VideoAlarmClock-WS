package main.providers;

import main.model.CommandSequence;
import main.model.ContentType;

public interface ICommandProvider {
	/*
	 *  Content commands creation
	 */
	CommandSequence GetContentCommand_SimpleVideo() throws Exception;
	CommandSequence GetContentCommand_InfoChannel(InfoChannel channel) throws Exception;
	CommandSequence GetContentCommand_CustomDailyYoutubePlaylist() throws Exception;
	
	/*
	 *  Steps commands creation
	 */
	String GetCommands_ForTimerPeriod() throws Exception;
	String GetCommands_ForWakingUp(CommandSequence contentCommands, ContentType contentType) throws Exception;
	String GetCommands_ForClosing(int mediasTotalDurationMinutes) throws Exception;
}
