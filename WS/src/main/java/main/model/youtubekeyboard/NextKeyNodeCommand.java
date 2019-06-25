package main.model.youtubekeyboard;

public class NextKeyNodeCommand {
	
	public KeyNode nextKeyNode;
	public KeyCharDirection charDirection;
	
	public NextKeyNodeCommand(KeyNode nextKeyNode, KeyCharDirection charDirection) {
		this.nextKeyNode = nextKeyNode;
		this.charDirection = charDirection;
	}
}
