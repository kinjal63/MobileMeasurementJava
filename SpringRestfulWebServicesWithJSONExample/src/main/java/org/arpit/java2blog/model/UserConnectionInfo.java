package org.arpit.java2blog.model;

public class UserConnectionInfo {
	private String userId;
	private String toUserId;
	private String bluetoothAddress;
	private String wifiAddress;
	
	public String getUserId() {
		return userId;
	}
	public String getToUserId() {
		return toUserId;
	}
	public String getBluetoothAddress() {
		return bluetoothAddress;
	}
	public String getWifiAddress() {
		return wifiAddress;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
	public void setBluetoothAddress(String bluetoothAddress) {
		this.bluetoothAddress = bluetoothAddress;
	}
	public void setWifiAddress(String wifiAddress) {
		this.wifiAddress = wifiAddress;
	}
}
