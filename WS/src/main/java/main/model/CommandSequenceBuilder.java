package main.model;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import main.exceptions.NotImplementedException;
import main.model.youtubekeyboard.YoutubeKeyboardCommandManager;

public class CommandSequenceBuilder {
	
	@Autowired
	private YoutubeKeyboardCommandManager youtubeKeyboardCommandManager;
	
	private CommandSequence commandSequence;
	
	private final char delimiter = '-'; 
	
	private CommandSequenceBuilder() {
		this.commandSequence = new CommandSequence();
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
	
	// TODO To be improved (without using the reset position, but directly go to the correct character)
	public CommandSequenceBuilder typeTextInYoutube(String text) throws NotImplementedException {
		this.resetPositionOnYoutubeKeyboard();
		for (char character : text.toCharArray()) {
			this.getSequenceForYoutubeCharacter(character).addCommand(PsButton.O).resetPositionOnYoutubeKeyboard();
		}
		
		//Press enter
		return this.addCommand(PsButton.Down).addCommand(PsButton.Down).addCommand(PsButton.O);
	}
	
	private CommandSequenceBuilder resetPositionOnYoutubeKeyboard() {
		return this.addCommand(PsButton.Right, 7).addCommand(PsButton.Up, 5);
	}
	
	/**
	 *  From the reset position, add the correct button sequence to get a specified character
	 * 
	 * @param c
	 * @return
	 * @throws NotImplementedException 
	 */
	private CommandSequenceBuilder getSequenceForYoutubeCharacter(char c) throws NotImplementedException {
		CharacterCommandSequence charSequence = CharacterCommandSequence.getBtnSequenceOfChar(c);
		
		if(charSequence == null)
			throw new NotImplementedException(String.format("The character %s has not been implemented in the youtube keyboard character-sequence converting function", c));
		
		return this.addAllCommands(charSequence.getBtnSequence());
	}

	public CommandSequenceBuilder browseToYoutube() {
		//Go to the applications folder
		return this.addCommand(PsButton.Right).addCommand(PsButton.Right).addCommand(PsButton.Right).addCommand(PsButton.Right).addCommand(PsButton.Right).addCommand(PsButton.O)
		// Select Youtube
		.addCommand(PsButton.SleepOnce).addCommand(PsButton.Right).addCommand(PsButton.Down)
		.addCommand(PsButton.Down).addCommand(PsButton.Down).addCommand(PsButton.Down).addCommand(PsButton.Down)
		.addCommand(PsButton.Down).addCommand(PsButton.Down).addCommand(PsButton.Down).addCommand(PsButton.O).addCommand(PsButton.SleepOnce).addCommand(PsButton.O);
	}
	
}
