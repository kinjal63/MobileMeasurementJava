package org.arpit.java2blog.model.db;

public class NearByUserDBModel {
	private Long userId;
	private String deviceToken;
	
	public Long getUserId() {
		return userId;
	}
	public String getDeviceToken() {
		return deviceToken;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
}
