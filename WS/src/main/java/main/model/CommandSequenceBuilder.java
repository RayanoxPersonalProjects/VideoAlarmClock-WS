package main.model;

import java.util.List;
import java.util.stream.Collectors;

import main.model.youtube.keyboard.IYoutubeKeyboardTextCommandFactory;
import main.model.youtube.keyboard.YoutubeKeyboardTextCommandFactory;

public class CommandSequenceBuilder {
		
	private IYoutubeKeyboardTextCommandFactory youtubeKeyboardCommandManager;
	
	private CommandSequence commandSequence;
	
	private final char delimiter = '-'; 
	
	private CommandSequenceBuilder() {
		this.commandSequence = new CommandSequence();
		this.youtubeKeyboardCommandManager = new YoutubeKeyboardTextCommandFactory();
	}
	
	public static CommandSequenceBuilder CreateCommandSequence() {
		return new CommandSequenceBuilder();
	}
	
	public CommandSequenceBuilder addCommand(PsButton cmd) {
		this.commandSequence.addBtnCommand(cmd);
		return this;
	}
	
	public CommandSequenceBuilder addAllCommands(PsButton [] commands) {
		for (PsButton psButton : commands) {
			this.commandSequence.addBtnCommand(psButton);
		}
		return this;
	}
	
	public CommandSequenceBuilder addClickCommand() {
		this.commandSequence.addBtnCommand(PsButton.X);
		return this;
	}
	
	/**
	 *  Add a button and repeat it X consecutive times. 
	 * 
	 * @param cmd
	 * @param times
	 * @return
	 */
	public CommandSequenceBuilder addCommand(PsButton cmd, int times) {
		for (int i = 0; i < times; i++) {
			this.commandSequence.addBtnCommand(cmd);
		}
		return this;
	}
	
	public String build() {
		String brutSequence = "{";
		List<String> stringCommandSequence = this.commandSequence.getBtnSequence().stream().map(PsButton::getBrutCharacterForMessage).map(String::valueOf).collect(Collectors.toList());
		brutSequence += String.join(String.valueOf(delimiter), stringCommandSequence);
		brutSequence += "}";
		return brutSequence;		
	}
	
	/*
	 *  Predefined command sequences
	 */
	
	public CommandSequenceBuilder typeTextInYoutube(String text) throws Exception {
		// Get the youtube commands to type the text
		youtubeKeyboardCommandManager.addCommandsFromText(this, text, 'A');
		
		//Press enter
		return this.addCommand(PsButton.Down).addCommand(PsButton.Down).addCommand(PsButton.O);
	}

	public CommandSequenceBuilder browseToYoutube() {
		//Go to the applications folder
		return this.addCommand(PsButton.Right).addCommand(PsButton.Right).addCommand(PsButton.Right).addCommand(PsButton.Right).addCommand(PsButton.Right).addCommand(PsButton.O)
		// Select Youtube
		.addCommand(PsButton.SleepOnce).addCommand(PsButton.Right).addCommand(PsButton.Down)
		.addCommand(PsButton.Down).addCommand(PsButton.Down).addCommand(PsButton.Down).addCommand(PsButton.Down)
		.addCommand(PsButton.Down).addCommand(PsButton.Down).addCommand(PsButton.Down).addCommand(PsButton.O).addCommand(PsButton.SleepOnce).addCommand(PsButton.O);
	}

	
	/*
	 *  Getters
	 */
	
	/**
	 *  Shouldn't be used other than in test place
	 *  
	 * @return
	 */
	public IYoutubeKeyboardTextCommandFactory getYoutubeKeyboardCommandManager() {
		return youtubeKeyboardCommandManager;
	}
}
