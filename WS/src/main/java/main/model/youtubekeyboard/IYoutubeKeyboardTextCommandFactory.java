package main.model.youtubekeyboard;

import main.model.CommandSequenceBuilder;

public interface IYoutubeKeyboardTextCommandFactory {
	CommandSequenceBuilder addCommandsFromText(CommandSequenceBuilder commandBuilder, String text, Character startPosition) throws Exception;
}
