package main.providers;

import org.springframework.stereotype.Component;

import main.exceptions.NotImplementedException;
import main.model.CommandSequenceBuilder;
import main.model.PsButton;

@Component
public class DataProvider implements IDataProvider {

	@Override
	public String GetContentCommand_SimpleVideo() throws NotImplementedException {
		throw new NotImplementedException("The method simple video has not implemented, and may never be implemented in the future.");
	}

	@Override
	public String GetContentCommand_InfoChannel(InfoChannel channel) throws NotImplementedException {
		CommandSequenceBuilder commandBrowserBuilder = CommandSequenceBuilder.CreateCommandSequence();
		commandBrowserBuilder.browseToYoutube().typeTextInYoutube(channel.getTextToType()).addCommand(PsButton.O);		
		return commandBrowserBuilder.build();
	}

	@Override
	public String GetContentCommand_CustomDailyYoutubePlaylist() throws NotImplementedException {
		throw new NotImplementedException("Youtube custom playlist feature not implemented yet");
	}

	
	
}
