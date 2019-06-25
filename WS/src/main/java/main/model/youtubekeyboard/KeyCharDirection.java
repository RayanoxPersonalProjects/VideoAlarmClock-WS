package main.model.youtubekeyboard;

public enum KeyCharDirection {
	Up('U'), 
	Down('D'), 
	Right('R'), 
	Left('L');
	
	private char character;
	
	private KeyCharDirection(char character) {
		this.character = character;
	}
}
