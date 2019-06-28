package main.model.youtube.keyboard;

import main.model.CommandSequenceBuilder;

public interface IYoutubeKeyboardTextCommandFactory {
	CommandSequenceBuilder addCommandsFromText(CommandSequenceBuilder commandBuilder, String text, Character startPosition) throws Exception;
}
