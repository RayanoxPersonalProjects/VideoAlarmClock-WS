package main.model.youtube.playlist;

import java.util.Calendar;

public class VideoItem {
	
	private final String id;
	private final String title;
	private Calendar creationDate;
	
	public VideoItem(String id, String title, Calendar creationDate) {
		this.creationDate = creationDate;
		this.title = title;
		this.id = id;
	}

	public VideoItem(String id, String title) {
		this.title = title;
		this.id = id;
	}
	
	public Calendar getCreationDate() {
		return creationDate;
	}
	public String getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
}
