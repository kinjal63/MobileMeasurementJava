package org.arpit.java2blog.model;

import java.util.ArrayList;

public class UserConnectionInfo {
	private long userId;
	private ArrayList<Long> remoteUserIds;
	
	public long getUserId() {
		return userId;
	}
	public ArrayList<Long> getRemoteUserIds() {
		return remoteUserIds;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public void setRemoteUserIds(ArrayList<Long> remoteUserIds) {
		this.remoteUserIds = remoteUserIds;
	}
}
