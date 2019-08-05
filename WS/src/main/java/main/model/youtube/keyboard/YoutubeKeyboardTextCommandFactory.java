package main.model.youtube.keyboard;

import java.util.ArrayList;
import java.util.HashMap;

import main.model.CommandSequenceBuilder;

public class YoutubeKeyboardTextCommandFactory implements IYoutubeKeyboardTextCommandFactory {

	private final KeyNode [][] firstKeyboardArray = new KeyNode [][]{
		{ new KeyNode('A'), new KeyNode('B'), new KeyNode('C'), new KeyNode('D'), new KeyNode('E'), new KeyNode('F'), new KeyNode('G'), null },
		{ new KeyNode('H'), new KeyNode('I'), new KeyNode('J'), new KeyNode('K'), new KeyNode('L'), new KeyNode('M'), new KeyNode('N'), new KeyNode() },
		{ new KeyNode('O'), new KeyNode('P'), new KeyNode('Q'), new KeyNode('R'), new KeyNode('S'), new KeyNode('T'), new KeyNode('U'), null },
		{ new KeyNode('V'), new KeyNode('W'), new KeyNode('X'), new KeyNode('Y'), new KeyNode('Z'), new KeyNode('-'), new KeyNode('\''), null },
		{ new KeyNode(' '), null, null, null, null, null, null, null }	
		//TODO To improve a few the algorithm, add this line instead of the one above
		//{ null, null, null, new KeyNode(' '), null, null, null, null }
		
		//The line below won't work with my architecture. Canceled because too much changes to do to make this work. 
		//{ new KeyNode(' '), new KeyNode(' '), new KeyNode(' '), new KeyNode(' '), new KeyNode(' '), new KeyNode(' '), new KeyNode(' '), new KeyNode(' ') }
	};
	
	private final KeyNode [][] secondKeyboardArray = new KeyNode [][]{
		{ new KeyNode('1'), new KeyNode('2'), new KeyNode('3'), new KeyNode('&'), new KeyNode('#'), new KeyNode('('), new KeyNode(')'), null },
		{ new KeyNode('4'), new KeyNode('5'), new KeyNode('6'), new KeyNode('@'), new KeyNode('!'), new KeyNode('?'), new KeyNode(':'), new KeyNode() },
		{ new KeyNode('7'), new KeyNode('8'), new KeyNode('9'), new KeyNode('0'), new KeyNode('.'), new KeyNode('_'), new KeyNode('"'), null },
		{ new KeyNode(' '), null, null, null, null, null, null, null }
		//TODO To improve a few the algorithm, add this line instead of the one above
		//{ null, null, null, new KeyNode(' '), null, null, null, null }
		
		//The line below won't work with my architecture. Canceled because too much changes to do to make this work.
		//{ new KeyNode(' '), new KeyNode(' '), new KeyNode(' '), new KeyNode(' '), new KeyNode(' '), new KeyNode(' '), new KeyNode(' '), new KeyNode(' ') }
	};
	
	private HashMap<Character, KeyNode> referenceMapToKeyNodes = new HashMap<Character, KeyNode>();
	private KeyNode spaceInFirstArrayReference;
	private KeyNode spaceInSecondArrayReference;
	private KeyNode bridgePointerInFirstArray;
	private KeyNode bridgePointerInSecondArray;
	
	
	public YoutubeKeyboardTextCommandFactory() {
		// Link every nodes to build a graph of keyNodes
		this.linkEveryNodeToBuildGraph(this.firstKeyboardArray);
		this.linkEveryNodeToBuildGraph(this.secondKeyboardArray);
		this.linkBridgeKeyNodes();
		
		// Set the reference to key nodes map (usefull to access to a specific node by its Character without browsing all the arrays each time)
		this.referenceKeyNodes();
		
		// Browse the graph to calculate all the best choice table (table that indicates which keyNode to go to for reaching the final targetted KeyNode)
		this.calculateBestDistancesMapForAllNodesOfArray(this.firstKeyboardArray);
		this.calculateBestDistancesMapForAllNodesOfArray(this.secondKeyboardArray);
		
		//Link arrays together
		this.linkMapsOfArrayNodesToReferenceNodesArray(this.firstKeyboardArray, this.secondKeyboardArray);
		this.linkMapsOfArrayNodesToReferenceNodesArray(this.secondKeyboardArray, this.firstKeyboardArray);
	}
	
	
	/*
	 *  Public access 
	 */


	@Override
	public CommandSequenceBuilder addCommandsFromText(CommandSequenceBuilder commandBuilder, String text, Character startPosition) throws Exception {
		KeyNode currentNode = getKeyNodeReferenced(startPosition, this.firstKeyboardArray);
		text = text.toUpperCase(); //There is no distinction between lower or upper case in youtube (Youtube PS Keyboard)
		
		KeyNode [][] currentArray = this.firstKeyboardArray; // Rustine pour les espaces
		
		for (Character currentCharTarget : text.toCharArray()) {
			KeyNode targetedNode = getKeyNodeReferenced(currentCharTarget, currentArray);
			
			if(currentNode.getCharacter().equals(currentCharTarget)) {
				commandBuilder.addClickCommand();
			}else {
				while(currentNode != targetedNode) {
					NextKeyNodeCommand nextCommandObj = currentNode.getClosestKeyNodeToReachCharacter(currentCharTarget);
					commandBuilder.addCommand(nextCommandObj.charDirection);
					
					currentNode = nextCommandObj.nextKeyNode;
				}
				commandBuilder.addClickCommand();
			}
			
			currentArray = isContainedNodeInArray(targetedNode, this.firstKeyboardArray) ? this.firstKeyboardArray : this.secondKeyboardArray;
		}
		
		return commandBuilder;
		
	}
	
	
	/*
	 *  Private tools to build complete key node graph
	 */
	
	


	private void calculateBestDistancesMapForAllNodesOfArray(KeyNode[][] keyNodeArray) {
		for (KeyNode[] keyNodesLine : keyNodeArray) {
			for (KeyNode keyNode : keyNodesLine) {
				if(keyNode != null && !keyNode.isBridgeKeyNode())
					this.calculateBestDistancesInArrayNodesToReachCharacter(keyNodeArray, keyNode.getCharacter());
			}
		}
	}

	private void calculateBestDistancesInArrayNodesToReachCharacter(KeyNode[][] keyBoardArray, Character characterToReach) {
		KeyNode nodeToReach = getKeyNodeReferenced(characterToReach, keyBoardArray);
		ArrayList<KeyNode> allAdjacentNodes = new ArrayList<KeyNode>();
		allAdjacentNodes.add(nodeToReach);
		
		while(!allAdjacentNodes.isEmpty()) {
			ArrayList<KeyNode> futureNoeudsAdjacents = new ArrayList<KeyNode>();
			
			for (KeyNode currentNode : allAdjacentNodes) {
				KeyNode upNode = currentNode.getNodeUp();
				KeyNode downNode = currentNode.getNodeDown();
				KeyNode leftNode = currentNode.getNodeLeft();
				KeyNode rightNode = currentNode.getNodeRight();
				
				DistanceNode distanceForCurrentNode = currentNode.getMapDistanceNodeForCharacter(characterToReach);
				DistanceNode distanceForAdjacentNode = new DistanceNode(distanceForCurrentNode.distance + 1, currentNode);
				
				if(upNode != null && !upNode.hasBeenMarqued()) {
					upNode.pushToGoToClosestMap(characterToReach, distanceForAdjacentNode);
					futureNoeudsAdjacents.add(upNode);					
				}
				
				if(downNode != null && !downNode.hasBeenMarqued()) {
					downNode.pushToGoToClosestMap(characterToReach, distanceForAdjacentNode);
					futureNoeudsAdjacents.add(downNode);
				}
				
				if(leftNode != null && !leftNode.hasBeenMarqued()) {
					leftNode.pushToGoToClosestMap(characterToReach, distanceForAdjacentNode);
					futureNoeudsAdjacents.add(leftNode);
				}
				
				if(rightNode != null && !rightNode.hasBeenMarqued() && !rightNode.isBridgeKeyNode()) {
					rightNode.pushToGoToClosestMap(characterToReach, distanceForAdjacentNode);
					futureNoeudsAdjacents.add(rightNode);
				}
				
				currentNode.setHasBeenMarqued(true);
			}
						
			
			// Transfert de la future liste de noeuds adjacents vers la liste courante
			allAdjacentNodes.clear();
			allAdjacentNodes.addAll(futureNoeudsAdjacents);
		}
		
		this.resetMarks(keyBoardArray);
	}
	
	
	
	private void resetMarks(KeyNode [][] nodeArray) {
		for (KeyNode[] keyNodesLineArray : nodeArray) {
			for (KeyNode keyNode : keyNodesLineArray) {
				if(keyNode != null)
					keyNode.setHasBeenMarqued(false);
			}
		}
	}
	
	/**
	 * Used to prevent the redondancy of characters (' ') in both array
	 * 
	 * @param character
	 * @param nodeArray
	 * @return
	 */
	private KeyNode getKeyNodeReferenced(Character character, KeyNode [][] nodeArray) {
		if(character.equals(' '))
			return nodeArray.equals(this.firstKeyboardArray) ? this.spaceInFirstArrayReference : this.spaceInSecondArrayReference;
		else
			return this.referenceMapToKeyNodes.get(character);
	}
	
	private boolean isContainedNodeInArray(KeyNode targetedNode, KeyNode[][] nodeArray) {
		for (KeyNode[] keyNodeLine : nodeArray) {
			for (KeyNode keyNode : keyNodeLine) {
				if(keyNode == targetedNode)
					return true;
			}
		}
		return false;
	}
	
	/**
	 *  Set a reference to every clickable node of the graph
	 */
	private void referenceKeyNodes() {
		for (KeyNode[] keyNodeLine : firstKeyboardArray) {
			for (KeyNode keyNode : keyNodeLine) {
				if(keyNode != null) {
					if(!keyNode.isBridgeKeyNode()) {
						if(keyNode.getCharacter().equals(' '))
							this.spaceInFirstArrayReference = keyNode;
						else
							this.referenceMapToKeyNodes.put(keyNode.getCharacter(), keyNode);
					}else 
						this.bridgePointerInFirstArray = keyNode;
				}
			}
		}
		for (KeyNode[] keyNodeLine : secondKeyboardArray) {
			for (KeyNode keyNode : keyNodeLine) {
				if(keyNode != null) {
					if(keyNode != null && !keyNode.isBridgeKeyNode()) {
						if(keyNode.getCharacter().equals(' '))
							this.spaceInSecondArrayReference = keyNode;
						else
							this.referenceMapToKeyNodes.put(keyNode.getCharacter(), keyNode);
					}else 
						this.bridgePointerInSecondArray = keyNode;
				}
			}
		}
	}
	

	private void linkBridgeKeyNodes() {
		KeyNode characterN = this.firstKeyboardArray[1][6], 
				character_doubleDot = this.secondKeyboardArray[1][6],
				bridgeNode_firstArray = this.firstKeyboardArray[1][7],
				bridgeNode_secondArray = this.secondKeyboardArray[1][7];
		
		bridgeNode_firstArray.setNodeKeyBridgeTarget(character_doubleDot);
		bridgeNode_secondArray.setNodeKeyBridgeTarget(characterN);
		
	}
	
	private void linkMapsOfArrayNodesToReferenceNodesArray(KeyNode[][] nodeArrayToMap, KeyNode[][] nodeArrayRefered) {
		
		//Link the two bridges pointers	
		this.bridgePointerInFirstArray.setNodeKeyBridgeTarget(this.bridgePointerInSecondArray);
		this.bridgePointerInSecondArray.setNodeKeyBridgeTarget(this.bridgePointerInFirstArray);
		
		// Prepare linking all the nodes to the brigde adjacent node ('N' or ':')
		KeyNode targetInCurrentArray = nodeArrayToMap == this.firstKeyboardArray ? getKeyNodeReferenced('N', nodeArrayToMap) : getKeyNodeReferenced(':', nodeArrayToMap);
		KeyNode bridge = nodeArrayToMap == this.firstKeyboardArray ? this.bridgePointerInFirstArray : bridgePointerInSecondArray;
		
		// Direct link bridge with adjacent node
		bridge.setNodeLeft(targetInCurrentArray);
		
		// Link all the nodes to the brigde adjacent node ('N' or ':')
		for (KeyNode[] keyNodeReferenceLine : nodeArrayRefered) {
			for (KeyNode keyNodeReference : keyNodeReferenceLine) {
				if(keyNodeReference == null || keyNodeReference.isBridgeKeyNode())
					continue;
				
				
				if(!keyNodeReference.getCharacter().equals(' ')) {
					for (KeyNode[] keyNodesToMapLine : nodeArrayToMap) {
						for (KeyNode keyNodeToMap : keyNodesToMapLine) {
							if(keyNodeToMap == null || keyNodeToMap.isBridgeKeyNode())
								continue;
							
							if (!keyNodeToMap.getCharacter().equals('N') && !keyNodeToMap.getCharacter().equals(':')) {
								Character charTarget = nodeArrayToMap == this.firstKeyboardArray ? 'N' : ':';
								keyNodeToMap.pushToGoToClosestMap(keyNodeReference.getCharacter(), keyNodeToMap.getMapDistanceNodeForCharacter(charTarget));
							} else {
								// Link the bridge adjacent node ('N' or ':') with its bridge (on the right)
								Character adjacentBridgeChar = keyNodeToMap.getCharacter();
								KeyNode adjacentToBridgeNode = getKeyNodeReferenced(adjacentBridgeChar, nodeArrayToMap == this.firstKeyboardArray ? this.firstKeyboardArray : this.secondKeyboardArray);
								KeyNode bridgeNode = adjacentToBridgeNode.getNodeRight();
	
								adjacentToBridgeNode.pushToGoToClosestMap(keyNodeReference.getCharacter(), new DistanceNode(bridgeNode));
							}
						}
					}
					
					//Create the map of the bridge
					bridge.pushToGoToClosestMap(keyNodeReference.getCharacter(), new DistanceNode(bridge.getNodeKeyBridgeTarget()));
				}
				
			}
		}
		
		//Map the bridge inside their own array
		for (KeyNode[] keyNodeLine : nodeArrayToMap) {
			for (KeyNode keyNodeInCurrentArray : keyNodeLine) {
				if(keyNodeInCurrentArray == null || keyNodeInCurrentArray.isBridgeKeyNode())
					continue;
				
				bridge.pushToGoToClosestMap(keyNodeInCurrentArray.getCharacter(), new DistanceNode(targetInCurrentArray));
			}
		}
	}
	
	private void linkEveryNodeToBuildGraph(KeyNode [][] keyBoardArray) {
		for (int i = 0; i< keyBoardArray.length; i++) {
			KeyNode[] keyNodeLine = keyBoardArray[i];
			
			for (int y = 0; y < keyNodeLine.length; y++) {
				KeyNode currentKeyNode = keyNodeLine[y];
				
				if(currentKeyNode == null || currentKeyNode.isBridgeKeyNode())
					continue;
				
				int indiceUp = i-1, indiceDown = i+1, indiceLeft = y-1, indiceRight = y+1;
				
				if(indiceUp >= 0) {
					KeyNode upNode = keyBoardArray[indiceUp][y];
					if(upNode != null) {
						currentKeyNode.setNodeUp(upNode);
					}
				}
				if(indiceDown < keyBoardArray.length) {
					KeyNode downNode = keyBoardArray[indiceDown][y];
					if(downNode != null) {
						currentKeyNode.setNodeDown(downNode);
					}
				}
				if(indiceLeft >= 0) {
					KeyNode leftNode = keyBoardArray[i][indiceLeft];
					if(leftNode != null) {
						currentKeyNode.setNodeLeft(leftNode);
					}
				}
				if(indiceRight < keyNodeLine.length) {
					KeyNode rightNode = keyBoardArray[i][indiceRight];
					if(rightNode != null) {
						currentKeyNode.setNodeRight(rightNode);
					}
				}
			}
		}
	}
}
