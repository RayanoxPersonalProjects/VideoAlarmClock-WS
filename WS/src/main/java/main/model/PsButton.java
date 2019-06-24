package main.model;

public enum PsButton {
	Up ('U'), 
	Down ('D'), 
	Right ('R'), 
	Left ('L'), 
	X ('X'), 
	O ('O'), 
	SleepOnce ('S');
	
	private char brutCharacterForMessage;
	
	private PsButton(char brutCharacterForMessage) {
		this.brutCharacterForMessage = brutCharacterForMessage;
	}
	
	public char getBrutCharacterForMessage() {
		return brutCharacterForMessage;
	}
}
