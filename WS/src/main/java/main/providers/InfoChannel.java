package main.providers;

public enum InfoChannel {
	France24("France 24 direct"),
	euronews_playlist("euronews");
	
	private String textToType;
	
	private InfoChannel(String textToType) {
		this.textToType = textToType;
	}
	
	public String getTextToType() {
		return textToType.toLowerCase();
	}
}
