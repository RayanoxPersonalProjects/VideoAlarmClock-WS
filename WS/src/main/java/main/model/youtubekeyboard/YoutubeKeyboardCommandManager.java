package main.model.youtubekeyboard;

import org.springframework.stereotype.Component;

@Component
public class YoutubeKeyboardCommandManager {

	private final KeyNode [][] firstKeyboardArray = new KeyNode [][]{
		{ new KeyNode('A'), new KeyNode('B'), new KeyNode('C'), new KeyNode('D'), new KeyNode('E'), new KeyNode('F'), new KeyNode('G'), null },
		{ new KeyNode('H'), new KeyNode('I'), new KeyNode('J'), new KeyNode('K'), new KeyNode('L'), new KeyNode('M'), new KeyNode('N'), new KeyNode() },
		{ new KeyNode('O'), new KeyNode('P'), new KeyNode('Q'), new KeyNode('R'), new KeyNode('S'), new KeyNode('T'), new KeyNode('U'), null },
		{ new KeyNode('V'), new KeyNode('W'), new KeyNode('X'), new KeyNode('Y'), new KeyNode('Z'), new KeyNode('-'), new KeyNode('\''), null },
		{ new KeyNode(' '), null, null, null, null, null, null, null }	
		//TODO To improve the algorithm, add this line instead of the one above
		//{ new KeyNode(' '), new KeyNode(' '), new KeyNode(' '), new KeyNode(' '), new KeyNode(' '), new KeyNode(' '), new KeyNode(' '), new KeyNode(' ') }
	};
	
	private final KeyNode [][] secondKeyboardArray = new KeyNode [][]{
		{ new KeyNode('1'), new KeyNode('2'), new KeyNode('3'), new KeyNode('&'), new KeyNode('#'), new KeyNode('('), new KeyNode(')'), null },
		{ new KeyNode('4'), new KeyNode('5'), new KeyNode('6'), new KeyNode('@'), new KeyNode('!'), new KeyNode('?'), new KeyNode(':'), new KeyNode() },
		{ new KeyNode('7'), new KeyNode('8'), new KeyNode('9'), new KeyNode('0'), new KeyNode('.'), new KeyNode('_'), new KeyNode('"'), null },
		{ new KeyNode(' '), null, null, null, null, null, null, null }
		//TODO To improve the algorithm, add this line instead of the one above
		//{ new KeyNode(' '), new KeyNode(' '), new KeyNode(' '), new KeyNode(' '), new KeyNode(' '), new KeyNode(' '), new KeyNode(' '), new KeyNode(' ') }
	};
	
	
	public YoutubeKeyboardCommandManager() {
		// Link every nodes to build a graph of keyNodes
		this.linkEveryNodeToBuildGraph(this.firstKeyboardArray);
		this.linkEveryNodeToBuildGraph(this.secondKeyboardArray);
		this.linkBridgeKeyNodes();
		
		// Browse the graph to calculate all the best choice table (table that indicates which keyNode to go to for reaching the final targetted KeyNode)
		
		int i = 0;
	}
	
	private void linkBridgeKeyNodes() {
		KeyNode characterN = this.firstKeyboardArray[1][6], 
				character_doubleDot = this.secondKeyboardArray[1][6],
				bridgeNode_firstArray = this.firstKeyboardArray[1][7],
				bridgeNode_secondArray = this.secondKeyboardArray[1][7];
		
		bridgeNode_firstArray.setNodeKeyBridgeTarget(character_doubleDot);
		bridgeNode_secondArray.setNodeKeyBridgeTarget(characterN);
		
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
