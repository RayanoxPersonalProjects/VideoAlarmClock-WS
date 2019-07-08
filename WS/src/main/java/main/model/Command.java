package main.model;

import main.exceptions.NotImplementedException;

public class Command {
	private DeviceAction deviceAction;
	
	// Parameter
	private Object commandParameter; // The command parameter, like the time period to maintain a button pressed, or any other parameter relative to the protocol documentation.
	
	private Command(DeviceAction deviceAction, Object actionPressTimeSeconds) {
		this.deviceAction = deviceAction;
		this.commandParameter = actionPressTimeSeconds;
	}
	
	
	public static Command Create(DeviceAction action, Integer actionPressTimeSeconds) {
		return new Command(action, actionPressTimeSeconds);
	}
	public static Command Create(DeviceAction action, String stringParameter) {
		return new Command(action, stringParameter);
	}
	public static Command Create(DeviceAction action) {
		return new Command(action, null);
	}
	
	
	
	public boolean isParameterSet() throws NotImplementedException {
		if(this.commandParameter == null)
			return false;
		
		if(this.commandParameter instanceof Integer)
			return ((Integer)this.commandParameter) > 0;
		else if(this.commandParameter instanceof String)
			return !((String)this.commandParameter).isEmpty();
		else 
			throw new NotImplementedException("The command parameter conversion has not been implemented for the type " + this.commandParameter.getClass().getSimpleName());
	}
	
	@Override
	public boolean equals(Object commandObject) {
		if(!commandObject.getClass().equals(this.getClass()))
			return false;
		
		Command command = (Command) commandObject;
		
		if(command.getParameter() != null && this.getParameter() == null
				|| command.getParameter() == null && this.getParameter() != null)
			return false;
		
		if(command.getParameter() != null && !command.getParameter().equals(this.commandParameter))
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
	
	public Object getParameter() {
		return this.commandParameter;
	}	
	
	public char getBrutCharacterForMessage() {
		return this.deviceAction.getBrutCharacterForMessage();
	}	
}
