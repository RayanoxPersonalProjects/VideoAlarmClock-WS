package main.model.youtube.playlist;

import java.time.Duration;
import java.util.Calendar;

public class VideoItem {
	
	private final String id;
	private final String title;
	private Calendar creationDate;
	private Duration duration;

	public VideoItem(String id, String title, Calendar creationDate, Duration duration) {
		this(id, title, creationDate);
		this.duration = duration;
	}
	
	public VideoItem(String id, String title, Calendar creationDate) {
		this(id, title);
		this.creationDate = creationDate;
	}

	public VideoItem(String id, String title) {
		this.title = title;
		this.id = id;
	}
	
	
	/*
	 *  Getters / Setters
	 */
	
	public void setDuration(Duration duration) {
		this.duration = duration;
	}
	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
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
	public Duration getDuration() {
		return duration;
	}
}
