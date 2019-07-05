package main.model;

public class Command {
	private DeviceAction deviceAction;
	private Integer actionPressTimeSeconds; // The time period to maintain a button pressed.
	
	private Command(DeviceAction deviceAction, Integer actionPressTimeSeconds) {
		this.deviceAction = deviceAction;
		this.actionPressTimeSeconds = actionPressTimeSeconds;
	}
	
	public static Command Create(DeviceAction action, Integer actionPressTimeSeconds) {
		return new Command(action, actionPressTimeSeconds);
	}
	public static Command Create(DeviceAction action) {
		return new Command(action, null);
	}
	
	public boolean isPressMaintained() {
		return this.actionPressTimeSeconds != null && this.actionPressTimeSeconds > 0;
	}
	
	@Override
	public boolean equals(Object commandObject) {
		if(!commandObject.getClass().equals(this.getClass()))
			return false;
		
		Command command = (Command) commandObject;
		
		if(command.getActionPressTimeSeconds() != null && this.getActionPressTimeSeconds() == null
				|| command.getActionPressTimeSeconds() == null && this.getActionPressTimeSeconds() != null)
			return false;
		
		if(command.getActionPressTimeSeconds() != null && !command.getActionPressTimeSeconds().equals(this.actionPressTimeSeconds))
			return false;
					
		if(!command.getDeviceAction().equals(this.deviceAction))
			return false;
		
		return true;
	}
	
	/*
	 *  Getters
	 */
	
	public DeviceAction getDeviceAction() {
		return this.deviceAction;
	}
	
	public Integer getActionPressTimeSeconds() {
		return this.actionPressTimeSeconds;
	}	
	
	public char getBrutCharacterForMessage() {
		return this.deviceAction.getBrutCharacterForMessage();
	}	
}
