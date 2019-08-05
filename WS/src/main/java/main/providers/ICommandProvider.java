package main.providers;

import main.model.CommandSequence;
import main.model.ContentType;
import main.model.dto.CommandDto;

public interface ICommandProvider {
	/*
	 *  Content commands creation
	 */
	CommandSequence GetContentCommand_SimpleVideo() throws Exception;
	CommandSequence GetContentCommand_InfoChannel(InfoChannel channel) throws Exception;
	CommandSequence GetContentCommand_CustomDailyYoutubePlaylist(CommandDto wrapper) throws Exception;
	
	/*
	 *  Steps commands creation
	 */
	String GetCommands_ForTimerPeriod() throws Exception;
	String GetCommands_ForWakingUp(CommandSequence contentCommands, ContentType contentType) throws Exception;
	String GetCommands_ForClosing(long mediasTotalDurationMinutes) throws Exception;
}
