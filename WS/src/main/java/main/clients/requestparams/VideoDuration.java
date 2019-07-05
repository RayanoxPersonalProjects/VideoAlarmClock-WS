package main.clients.requestparams;

public enum VideoDuration {
	
	Any ("any", null),
	Short("short", 4), // short – Only include videos that are less than four minutes long.
	Medium("medium", 20), // medium – Only include videos that are between four and 20 minutes long (inclusive).
	Long("long", null); // long – Only include videos longer than 20 minutes.
	
	private String textCode;
	private Integer lessThanMinutes;
	
	private VideoDuration(String textCode, Integer lessThanMinutes) {
		this.textCode = textCode;
		this.lessThanMinutes = lessThanMinutes;
	}
	
	public String getTextCode() {
		return textCode;
	}
	
	public Integer getLessThanMinutes() {
		return lessThanMinutes;
	}
	
	public static VideoDuration getVideoDurationForMaxMinutesValue(Integer maxValue) {
		if(maxValue == null)
			return Any;
		
		VideoDuration result = null;
		
		for (VideoDuration videoDuration : VideoDuration.values()) {
			if(videoDuration.getLessThanMinutes() == null)
				continue;
			
			if(maxValue.compareTo(videoDuration.getLessThanMinutes()) <= 0) {
				if(result != null && videoDuration.getLessThanMinutes().compareTo(result.getLessThanMinutes()) >= 0)
					continue;
				result = videoDuration;
			}
		}
		
		return result == null ? Long : result;
	}
}
