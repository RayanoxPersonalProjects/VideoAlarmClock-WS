package main.model.youtubekeyboard;

import java.util.HashMap;

public class KeyNode {
	
	private Character character;

	private KeyNode nodeUp;
	private KeyNode nodeDown;
	private KeyNode nodeLeft;
	private KeyNode nodeRight;
	
	/**
	 *  Map that tell which Keynode is the closest to reach the expected character.
	 */
	private HashMap<Character, KeyNode> mapGoToClosest;
	
	public KeyNode(Character character) {
		this.character = character;
		this.mapGoToClosest = new HashMap<Character, KeyNode>();
	}
	
	//TODO A verifier la coherence
	public void pushToGoToClosestMap(Character key, KeyNode value) {
		this.mapGoToClosest.put(key, value);
	}
	
	public NextKeyNodeCommand getClosestKeyNodeToReachCharacter(Character character) throws Exception {
		KeyNode nextKeyNode = this.mapGoToClosest.get(character);
				
		if(nodeUp != null && nodeUp.getCharacter().equals(nextKeyNode.getCharacter())) {
			return new NextKeyNodeCommand(nextKeyNode, KeyCharDirection.Up);
		}else if(nodeDown != null && nodeDown.getCharacter().equals(nextKeyNode.getCharacter())) {
			return new NextKeyNodeCommand(nextKeyNode, KeyCharDirection.Down);
		}if(nodeLeft != null && nodeLeft.getCharacter().equals(nextKeyNode.getCharacter())) {
			return new NextKeyNodeCommand(nextKeyNode, KeyCharDirection.Left);
		}if(nodeRight != null && nodeRight.getCharacter().equals(nextKeyNode.getCharacter())) {
			return new NextKeyNodeCommand(nextKeyNode, KeyCharDirection.Right);
		}else {
			throw new Exception("A development conception mistake is present. No adjacent node is the closest node to go detected.");
		}
	}
	
	
	
	
	/*
	 *  Setters / Getters
	 */
	
	public Character getCharacter() {
		return character;
	}
	
}
