package main.model.youtube.keyboard;

import main.model.Command;

public class NextKeyNodeCommand {
	
	public KeyNode nextKeyNode;
	public Command charDirection;
	
	public NextKeyNodeCommand(KeyNode nextKeyNode, Command charDirection) {
		this.nextKeyNode = nextKeyNode;
		this.charDirection = charDirection;
	}
}
