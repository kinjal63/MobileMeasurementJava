package com.hk.mm.vo;

public class DUser {

	private String message;
	private String email;
	private String authToken;
	private String userId;
	private String toUserId;
	private String bluetoothAddress;
	private Boolean accept;
	private String rssi;
	private String latitude;
	private String operatorName;
	private String deviceId;
	private String longitude;

	private Long mobileTx;
	private Long mobileRx;
	private Long wifiTx;
	private Long wifiRx;
	public Long getMobileTx() {
		return mobileTx;
	}

	public void setMobileTx(Long mobileTx) {
		this.mobileTx = mobileTx;
	}

	public Long getMobileRx() {
		return mobileRx;
	}

	public void setMobileRx(Long mobileRx) {
		this.mobileRx = mobileRx;
	}

	public Long getWifiTx() {
		return wifiTx;
	}

	public void setWifiTx(Long wifiTx) {
		this.wifiTx = wifiTx;
	}

	public Long getWifiRx() {
		return wifiRx;
	}

	public void setWifiRx(Long wifiRx) {
		this.wifiRx = wifiRx;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	private String country;
	

	public String getBluetoothAddress() {
		return bluetoothAddress;
	}

	public void setBluetoothAddress(String bluetoothAddress) {
		this.bluetoothAddress = bluetoothAddress;
	}

	public Boolean getAccept() {
		return accept;
	}

	public void setAccept(Boolean accept) {
		this.accept = accept;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	private String fromTime;
	private String toTime;

	public String getEmail() {
		return email;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public String getRssi() {
		return rssi;
	}

	public void setRssi(String rssi) {
		this.rssi = rssi;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

}
