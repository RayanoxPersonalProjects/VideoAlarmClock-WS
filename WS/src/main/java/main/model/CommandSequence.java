package main.model;

import java.util.ArrayList;

public class CommandSequence {
	
	private ArrayList<PsButton> btnSequence;
	
	public CommandSequence() {
		this.btnSequence = new ArrayList<PsButton>();
	}
	
	public void addBtnCommand(PsButton btn) {
		this.btnSequence.add(btn);
	}
	
	public ArrayList<PsButton> getBtnSequence() {
		return btnSequence;
	}
	
	public boolean compare(CommandSequence seqCompared) {
		if(btnSequence.size() != seqCompared.getBtnSequence().size())
			return false;
		for(int i = 0; i < btnSequence.size(); i++) {
			if(!btnSequence.get(i).equals(seqCompared.getBtnSequence().get(i)))
				return false;
		}
		return true;
	}
	
}
