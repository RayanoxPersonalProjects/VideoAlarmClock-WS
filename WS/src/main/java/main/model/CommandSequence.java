package main.model;

import java.util.ArrayList;

public class CommandSequence {
	
	private ArrayList<Command> btnSequence;
	
	public CommandSequence() {
		this.btnSequence = new ArrayList<Command>();
	}
	
	public void addBtnCommand(Command btn) {
		this.btnSequence.add(btn);
	}
	
	public void addBtnCommand(DeviceAction action) {
		this.addBtnCommand(action, null);
	}
	
	public void addBtnCommand(DeviceAction action, Integer secondsDuring) {
		this.btnSequence.add(Command.Create(action, secondsDuring));
	}
	
	public ArrayList<Command> getBtnSequence() {
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
