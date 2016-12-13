package org.arpit.java2blog.model;

public class RssiREQ {

	private int id;
	private String deviceId;
	private String rssi;
	
	public int getId() {
		return id;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public String getRssi() {
		return rssi;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public void setRssi(String rssi) {
		this.rssi = rssi;
	}
	
}
