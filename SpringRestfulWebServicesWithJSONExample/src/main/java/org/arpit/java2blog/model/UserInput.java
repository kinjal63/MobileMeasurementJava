package org.arpit.java2blog.model;

public class UserInput {
	private long userId;
	private long toUserId;
	private String bluetoothAddress;
	private String wifiAddress;
	private boolean accept;
	
	public String getBluetoothAddress() {
		return bluetoothAddress;
	}
	public String getWifiAddress() {
		return wifiAddress;
	}
	public boolean isAccept() {
		return accept;
	}
	public long getUserId() {
		return userId;
	}
	public long getToUserId() {
		return toUserId;
	}
	public void setFromUserId(long userId) {
		this.userId = userId;
	}
	public void setToUserId(long toUserId) {
		this.toUserId = toUserId;
	}
	public void setBluetoothAddress(String bluetoothAddress) {
		this.bluetoothAddress = bluetoothAddress;
	}
	public void setWifiAddress(String wifiAddress) {
		this.wifiAddress = wifiAddress;
	}
	public void setAccept(boolean accept) {
		this.accept = accept;
	}
}