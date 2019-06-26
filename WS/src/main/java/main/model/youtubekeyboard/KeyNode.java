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
	
	private final boolean isBridgeKeyNode;
	private KeyNode nodeKeyBridgeTarget;
	
	
	/**
	 * Constructor of bridge key node
	 */
	public KeyNode() {
		this.character = null;
		this.isBridgeKeyNode = true;
	}
	
	/**
	 * Constructor of character keyNode
	 * 
	 * @param character
	 */
	public KeyNode(Character character) {
		this.character = character;
		this.mapGoToClosest = new HashMap<Character, KeyNode>();
		this.isBridgeKeyNode = false;
		this.setNodeKeyBridgeTarget(null);
	}
	
	//TODO A verifier la coherence
	public void pushToGoToClosestMap(Character key, KeyNode value) {
		this.mapGoToClosest.put(key, value);
	}
	
	//Celui qui va appeler cette methode devra surement gerer le cas de l'ajout de plusieurs NextKeyNodeCommand à la suite pour faire le lien entre les deux claviers 
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

	public boolean isBridgeKeyNode() {
		return isBridgeKeyNode;
	}
	
	public void setNodeUp(KeyNode nodeUp) {
		this.nodeUp = nodeUp;
	}
	public void setNodeDown(KeyNode nodeDown) {
		this.nodeDown = nodeDown;
	}
	public void setNodeLeft(KeyNode nodeLeft) {
		this.nodeLeft = nodeLeft;
	}
	public void setNodeRight(KeyNode nodeRight) {
		this.nodeRight = nodeRight;
	}

	public KeyNode getNodeKeyBridgeTarget() {
		return nodeKeyBridgeTarget;
	}

	public void setNodeKeyBridgeTarget(KeyNode nodeKeyBridgeTarget) {
		this.nodeKeyBridgeTarget = nodeKeyBridgeTarget;
	}
	
}
