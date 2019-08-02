package main.model;

public enum DeviceAction {
	Up ('U'), 
	Down ('D'), 
	Right ('R'), 
	Left ('L'), 
	X ('X'), 
	O ('O'), 
	PS_Home('H'),
	
	TV_Power('P'),
	Volum_Up('u'),
	Volum_Down('d'),
	
	Sleep ('S'),
	Shutdown_Device('K'), 
	HTTP_GET('G');
	
	private char brutCharacterForMessage;
	
	private DeviceAction(char brutCharacterForMessage) {
		this.brutCharacterForMessage = brutCharacterForMessage;
	}
	
	public char getBrutCharacterForMessage() {
		return brutCharacterForMessage;
	}
}
