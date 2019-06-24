package main.model;

public class CommandSequenceBuilder {
	
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
	
	public String build() {
		String brutSequence = "{";
		String [] stringCommandSequence = (String []) this.commandSequence.getBtnSequence().stream().map(PsButton::toString).toArray();
		brutSequence += String.join(String.valueOf(delimiter), stringCommandSequence);
		brutSequence += "}";
		return brutSequence;		
	}
	
}
