package org.arpit.java2blog.model;

public class AppData {
	private long userId;
	private AppDetail[] appDetail;
	
	public long getUserId() {
		return userId;
	}
	public AppDetail[] getAppDetail() {
		return appDetail;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public void setAppDetail(AppDetail[] appDetail) {
		this.appDetail = appDetail;
	}
}
