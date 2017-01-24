package com.hk.mm.entity;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "data_usage")
public class DataUsage {

	private long dataUsageId;

	private User user;

	private String deviceId;
	private String country;
	private long mobileRx;
	private long mobileTx;
	private long wifiRx;
	private long wifiTx;
	private double longitude;
	private double latitude;
	private String operatorName;
	private Date timestamp;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public long getDataUsageId() {
		return dataUsageId;
	}

	public void setDataUsageId(long dataUsageId) {
		this.dataUsageId = dataUsageId;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "country")
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "deviceId")
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Column(name = "mobileRx")
	public long getMobileRx() {
		return mobileRx;
	}

	public void setMobileRx(long mobileRx) {
		this.mobileRx = mobileRx;
	}

	@Column(name = "mobileTx")
	public long getMobileTx() {
		return mobileTx;
	}

	public void setMobileTx(long mobileTx) {
		this.mobileTx = mobileTx;
	}

	@Column(name = "wifiRx")
	public long getWifiRx() {
		return wifiRx;
	}

	public void setWifiRx(long wifiRx) {
		this.wifiRx = wifiRx;
	}

	@Column(name = "wifiTx")
	public long getWifiTx() {
		return wifiTx;
	}

	public void setWifiTx(long wifiTx) {
		this.wifiTx = wifiTx;
	}

	@Column(name = "longitude")
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Column(name = "latitude")
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Column(name = "operator_name")
	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	@Column(name = "timestamp", columnDefinition = "DATETIME")
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
