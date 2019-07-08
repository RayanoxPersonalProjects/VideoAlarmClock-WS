package main.model;

import java.util.List;
import java.util.stream.Collectors;

import main.model.youtube.keyboard.IYoutubeKeyboardTextCommandFactory;
import main.model.youtube.keyboard.YoutubeKeyboardTextCommandFactory;

public class CommandSequenceBuilder {
		
	private IYoutubeKeyboardTextCommandFactory youtubeKeyboardCommandManager;
	
	private CommandSequence commandSequence;
	
	public static final char delimiter = '-'; 
	
	private CommandSequenceBuilder(IYoutubeKeyboardTextCommandFactory youtubeKeyboardCommandManager) {
		this.youtubeKeyboardCommandManager = youtubeKeyboardCommandManager;
		this.commandSequence = new CommandSequence();
	}
	
	private CommandSequenceBuilder() {
		this(new YoutubeKeyboardTextCommandFactory());
	}
	
	/**
	 * 
	 * @param needYoutubeKeyboard if false, it optimize this object creation because the 'youtubeKeyboardCommandManager' costs because of a complex algorithm
	 * @return
	 */
	public static CommandSequenceBuilder CreateCommandSequence(boolean needYoutubeKeyboard) {
		return needYoutubeKeyboard ? new CommandSequenceBuilder() : new CommandSequenceBuilder(null);
	}
	
	public CommandSequenceBuilder addCommand(Command cmd) {
		this.commandSequence.addBtnCommand(cmd);
		return this;
	}
	
	public CommandSequenceBuilder addCommand(DeviceAction action) {
		this.commandSequence.addBtnCommand(action);
		return this;
	}
	
	public CommandSequenceBuilder addAllCommands(Command [] commands) {
		for (Command psButton : commands) {
			this.commandSequence.addBtnCommand(psButton);
		}
		return this;
	}
	public CommandSequenceBuilder addAllCommands(CommandSequence commands) {
		for (Command psButton : commands.getBtnSequence()) {
			this.commandSequence.addBtnCommand(psButton);
		}
		return this;
	}
	
	public CommandSequenceBuilder addClickCommand() {
		this.commandSequence.addBtnCommand(DeviceAction.X);
		return this;
	}
	
	/**
	 *  Add a button and repeat it X consecutive times. 
	 * 
	 * @param cmd
	 * @param times
	 * @return
	 */
	public CommandSequenceBuilder addCommand(Command cmd, int times) {
		for (int i = 0; i < times; i++) {
			this.commandSequence.addBtnCommand(cmd);
		}
		return this;
	}
	
	/**
	 * Here is converted the Command sequence object into a String
	 * @return
	 */
	public String build() {
		String brutSequence = "";
		
		List<String> stringCommandSequence = this.commandSequence.getBtnSequence()
				.stream()
				.map( command -> {
					return command.isPressMaintained() ?
							command.getBrutCharacterForMessage() + "," + command.getActionPressTimeSeconds()
							: String.valueOf(command.getBrutCharacterForMessage());
				})
				.collect(Collectors.toList());
		
		brutSequence += String.join(String.valueOf(delimiter), stringCommandSequence);
		return brutSequence;		
	}
	
	/*
	 *  Predefined command sequences
	 */
	
	public CommandSequenceBuilder typeTextInYoutube(String text) throws Exception {
		// Get the youtube commands to type the text
		youtubeKeyboardCommandManager.addCommandsFromText(this, text, 'A');
		
		//Press enter
		return this.addCommand(DeviceAction.Down).addCommand(DeviceAction.Down).addCommand(DeviceAction.O);
	}

	public CommandSequenceBuilder browseToYoutube() {
		//Go to the applications folder
		return this.addCommand(DeviceAction.Right).addCommand(DeviceAction.Right).addCommand(DeviceAction.Right).addCommand(DeviceAction.Right).addCommand(DeviceAction.Right).addCommand(DeviceAction.O)
		// Select Youtube
		.addCommand(DeviceAction.Sleep).addCommand(DeviceAction.Right).addCommand(DeviceAction.Down)
		.addCommand(DeviceAction.Down).addCommand(DeviceAction.Down).addCommand(DeviceAction.Down).addCommand(DeviceAction.Down)
		.addCommand(DeviceAction.Down).addCommand(DeviceAction.Down).addCommand(DeviceAction.Down).addCommand(DeviceAction.O).addCommand(DeviceAction.Sleep).addCommand(DeviceAction.O);
	}

	public CommandSequenceBuilder sleep(int i) {
		return this.addCommand(Command.Create(DeviceAction.Sleep, i));
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
	
	public CommandSequence getCommandSequence() {
		return commandSequence;
	}
}
