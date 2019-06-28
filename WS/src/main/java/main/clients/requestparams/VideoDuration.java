package main.clients.requestparams;

public enum VideoDuration {
	
	Any ("any"),
	Short("short"),
	Medium("medium"),
	Long("long");
	
	private String textCode;
	
	private VideoDuration(String textCode) {
		this.textCode = textCode;
	}
	
	public String getTextCode() {
		return textCode;
	}
	
}
