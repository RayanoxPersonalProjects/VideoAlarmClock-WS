package main.model;

public enum ContentType {
	
	INFO_CHANNEL('I'),
	CUSTOM_DAILY_YOUTUBE_PLAYLIST('Y');
	
	
	private char code;
	
	private ContentType(char code) {
		this.code = code;
	}
	
	public static ContentType valueOf(char code) {
		for(ContentType val : ContentType.values()) {
			if(val.code == code)
				return val;
		}
		return null;
	}
}
