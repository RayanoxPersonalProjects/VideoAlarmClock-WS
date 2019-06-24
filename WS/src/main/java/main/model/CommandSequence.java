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
	
}
