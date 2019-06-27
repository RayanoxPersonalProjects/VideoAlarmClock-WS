package main.model.youtubekeyboard;

/**
 * This object is used to store the distance information to aim a character node 
 * 
 * @author rbenhmidane
 *
 */
public class DistanceNode {
	public int distance;
	public KeyNode nextKeyNodeClosestDistance;
	
	public DistanceNode(int distance) {
		this.distance = distance;
	}
	
	public DistanceNode(int distance, KeyNode nextNode) {
		this.distance = distance;
		this.nextKeyNodeClosestDistance = nextNode;
	}

	public DistanceNode(KeyNode nextNode) {
		this.nextKeyNodeClosestDistance = nextNode;
	} 
}
