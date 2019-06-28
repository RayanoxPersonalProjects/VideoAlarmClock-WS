package main.model.youtube.keyboard;

import main.model.PsButton;

public class NextKeyNodeCommand {
	
	public KeyNode nextKeyNode;
	public PsButton charDirection;
	
	public NextKeyNodeCommand(KeyNode nextKeyNode, PsButton charDirection) {
		this.nextKeyNode = nextKeyNode;
		this.charDirection = charDirection;
	}
}
