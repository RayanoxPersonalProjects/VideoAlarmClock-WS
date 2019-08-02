package main.providers;

import java.sql.Time;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import main.controllers.CommandController;
import main.controllers.InfosController;
import main.controllers.OperationController;
import main.exceptions.NotImplementedException;
import main.model.Command;
import main.model.CommandSequence;
import main.model.CommandSequenceBuilder;
import main.model.ContentType;
import main.model.DeviceAction;
import main.utils.Helper;


/**
 *  Commands sequences building
 */

@Component
public class CommandProvider implements ICommandProvider {


	@Autowired
	DataStorage dataStorage;

	
	/*
	 *  Commands by formula
	 */
	
	@Override
	public CommandSequence GetContentCommand_SimpleVideo() throws NotImplementedException {
		throw new NotImplementedException("The method simple video has not implemented, and may never be implemented in the future.");
	}

	@Override
	public CommandSequence GetContentCommand_InfoChannel(InfoChannel channel) throws Exception {
		CommandSequenceBuilder commandBrowserBuilder = CommandSequenceBuilder.CreateCommandSequence(true);
		commandBrowserBuilder.browseToYoutube().typeTextInYoutube(channel.getTextToType()).addCommand(DeviceAction.O);		
		return commandBrowserBuilder.getCommandSequence();
	}

	@Override
	public CommandSequence GetContentCommand_CustomDailyYoutubePlaylist() throws NotImplementedException {
		CommandSequenceBuilder commandBrowserBuilder = CommandSequenceBuilder.CreateCommandSequence(true);
		
		//TODO Code the access to the custom channel from Youtube on the PS4
		//throw new NotImplementedException("Youtube custom playlist feature not implemented yet (I have to code the access to the custom channel from Youtube on the PS4)");
		
		return commandBrowserBuilder.getCommandSequence();
	}
	
	
	
	/*
	 *  Common commands
	 */
	
	
	public String GetCommands_ForTimerPeriod() throws Exception {
		CommandSequenceBuilder commandBrowserBuilder = CommandSequenceBuilder.CreateCommandSequence(false);
		
		// Sleep during the timer period
		Integer minutesToWait = (Integer) this.dataStorage.getData(InfosController.CONF_SHUTDOWN_TIMER_VALUE_PROPERTY_NAME, Integer.class, InfosController.DEFAULT_SHUTDOWN_TIMER_VALUE);
		commandBrowserBuilder.sleep(minutesToWait * 60);
		
		// Close the TV
		commandBrowserBuilder.addCommand(Command.Create(DeviceAction.TV_Power));
		
		// Close the playstation
		//TODO To code at home
		
		return commandBrowserBuilder.build();
	}
	
	public String GetCommands_ForWakingUp(CommandSequence contentCommands, ContentType contentType) throws Exception {
		CommandSequenceBuilder commandBrowserBuilder = CommandSequenceBuilder.CreateCommandSequence(false);
		
		// Sleep the time needed to wakeup at defined time
		Integer timerMinutesToWait = (Integer) this.dataStorage.getData(InfosController.CONF_SHUTDOWN_TIMER_VALUE_PROPERTY_NAME, Integer.class, InfosController.DEFAULT_SHUTDOWN_TIMER_VALUE);
		Time timeWakeUp = (Time) this.dataStorage.getData(InfosController.CONF_ALARM_CLOCK_PROPERTY_NAME, Time.class, InfosController.DEFAULT_ALARM_TIME);
		int nightSleepingDurationSeconds = computeTimeToSleepForNight(timeWakeUp, timerMinutesToWait);
		commandBrowserBuilder.sleep(nightSleepingDurationSeconds);
		
		// Do a specific job, related to the contentType
		processSpecificJob(commandBrowserBuilder, contentType);
		
		// Open the Playstation
		commandBrowserBuilder.addCommand(DeviceAction.PS_Home);
		
		// Go to the main menu (case if I was somewhere else, like already on Youtube or an other app)
		//TODO To code at home
		
		// Add the 'contentCommands' commands to go to Youtube for example and search for the targeted video
		commandBrowserBuilder.addAllCommands(contentCommands);
		
		// Click on the video, then pause it
		//TODO To code at home

		// Enable sound out loud
		Integer volumeLevel = (Integer) this.dataStorage.getData(InfosController.CONF_VOLUM_PROPERTY_NAME, Integer.class, InfosController.DEFAULT_VOLUM);
		setSoundConfiguration(commandBrowserBuilder, volumeLevel);
		
		// Open the TV
		commandBrowserBuilder.addCommand(DeviceAction.TV_Power);
		
		// Wait few seconds (time to wait for the TV to be open)
		commandBrowserBuilder.sleep(10);
		
		// Click Play
		commandBrowserBuilder.addClickCommand();
		
		return commandBrowserBuilder.build();
	}
	

	public String GetCommands_ForClosing(int mediasTotalDurationMinutes) throws Exception {
		CommandSequenceBuilder commandBrowserBuilder = CommandSequenceBuilder.CreateCommandSequence(false);
		
		// Sleep during the time all the videos will take
		// TODO when I'll have the second PS remote control, I will konw more about the PS4 behaviours switching between the both remote controls, and probably I will enhance the code
		commandBrowserBuilder.sleep(mediasTotalDurationMinutes*60);
		
		// Disable sound out loud
		setSoundConfiguration(commandBrowserBuilder, 0);
		
		// Shutdown the device (Arduino)
		commandBrowserBuilder.addCommand(DeviceAction.Shutdown_Device);
		
		return commandBrowserBuilder.build();
	}
	
	
	
	/*
	 *  Private methods
	 */
	


	private void processSpecificJob(CommandSequenceBuilder commandBrowserBuilder, ContentType contentType) {
		switch(contentType) {
			case CUSTOM_DAILY_YOUTUBE_PLAYLIST:
				commandBrowserBuilder.sendHttpGetRequest(OperationController.UPDATE_PLAYLIST_PATH);
				break;
			default:
				return;
		}
	}
	
	private void setSoundConfiguration(CommandSequenceBuilder commandBrowserBuilder, int volumeLevel) {
		// Set the sound volume to 0, then increase it to reach the targeted volume level
		//TODO To code at home (check the second count)
		commandBrowserBuilder.addCommand(Command.Create(DeviceAction.Volum_Down, 10));
		commandBrowserBuilder.addCommand(Command.Create(DeviceAction.Volum_Up), 25);
		
		// Set the 'enable sound out of headset' box to checked.
		//TODO To code at home
	}
	
	/**
	 * 
	 * @param timeWakeUp
	 * @param timerMinutesToWait
	 * @return the amount of seconds to sleep
	 */
	private int computeTimeToSleepForNight(Time timeWakeUp, Integer timerMinutesToWait) {
		Calendar cal = Calendar.getInstance();
		Calendar wakeUpTime = Calendar.getInstance();
		wakeUpTime.set(Calendar.HOUR_OF_DAY, timeWakeUp.getHours());
		wakeUpTime.set(Calendar.MINUTE, timeWakeUp.getMinutes());
		wakeUpTime.set(Calendar.SECOND, timeWakeUp.getSeconds());
		
		if(wakeUpTime.before(cal))
			wakeUpTime.add(Calendar.DAY_OF_MONTH, 1);
		
		Long totalTimeSecondsBeforeWakeup = (wakeUpTime.getTimeInMillis() - cal.getTimeInMillis()) / 1000;
		int secondsTimer = timerMinutesToWait * 60;
		
		return totalTimeSecondsBeforeWakeup.intValue() - secondsTimer;
	}
	
}
