package main.clients.requestparams;

import java.time.Duration;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.api.client.util.DateTime;

public class VideoDetails {
	public Duration videoDuration;
	public Calendar creationDate;
	
	public VideoDetails(String duration, DateTime creationDate) throws Exception {
		this.creationDate = Calendar.getInstance();
		this.creationDate.setTimeInMillis(creationDate.getValue());
		
//		this.videoDuration = parseGoogleTimeString(duration);
		this.videoDuration = Duration.parse(duration);
	}
	
	
	/**
	 * @deprecated Same as using Duration.parse
	 * 
	 * @param timeDuration
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	private Duration parseGoogleTimeString(String timeDuration) throws Exception {
		Duration timeResult;
		long secondsTotal = 0;
		
		String [] timeSplitted = timeDuration.split("\\D+");
		
		Pattern pattern = Pattern.compile("(\\D+)");
		Matcher matcher = pattern.matcher(timeDuration);

		
		if(timeSplitted.length < 1)
			throw new Exception("The string time format received is not correct. Please, see the Youtube API doc.");
		
		matcher.find(); //To skip the first useless chars
		for(int i = 1; i < timeSplitted.length; i++) {
			if(!matcher.find())
				break;
			
			Integer value = Integer.valueOf(timeSplitted[i]);
			char splitChar = timeDuration.substring(matcher.start(), matcher.end()).charAt(0);
			//char splitChar = matcher.group(1).charAt(0);  -->  Works as well
			
			switch(splitChar) {
				case 'H':
					secondsTotal += value * 3600;
					break;
				case 'M':
					secondsTotal += value * 60;					
					break;
				case 'S':
					secondsTotal += value;
					break;
				default:
					throw new Exception(String.format("This character (%c) is not managed by the custom conversion, please update the code to adapt it according to the Youtube Google documentation.", splitChar));
			}
		}
		
		timeResult = Duration.ofSeconds(secondsTotal);
		return timeResult;
	}
}
