package org.arpit.java2blog.model;

public class UserTimeModel {
	private long userId;
	private String fromTime;
	private String toTime;
	
	public long getUserId() {
		return userId;
	}
	public String getFromTime() {
		return fromTime;
	}
	public String getToTime() {
		return toTime;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
	public void setToTime(String toTime) {
		this.toTime = toTime;
	}
}