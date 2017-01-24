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
@Table(name = "call_logs")
public class CallLogs {

	private long callLogId;

	private User user;

	private String country;
	private long total_mobileRx;
	private long total_mobileTx;
	private long total_wifiRx;
	private long total_wifiTx;
	private Date timestamp;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public long getCallLogId() {
		return callLogId;
	}

	public void setCallLogId(long callLogId) {
		this.callLogId = callLogId;
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

	@Column(name = "total_mobileRx")
	public long getTotal_mobileRx() {
		return total_mobileRx;
	}

	public void setTotal_mobileRx(long total_mobileRx) {
		this.total_mobileRx = total_mobileRx;
	}

	@Column(name = "total_mobileTx")
	public long getTotal_mobileTx() {
		return total_mobileTx;
	}

	public void setTotal_mobileTx(long total_mobileTx) {
		this.total_mobileTx = total_mobileTx;
	}

	@Column(name = "total_wifiRx")
	public long getTotal_wifiRx() {
		return total_wifiRx;
	}

	public void setTotal_wifiRx(long total_wifiRx) {
		this.total_wifiRx = total_wifiRx;
	}

	@Column(name = "total_wifiTx")
	public long getTotal_wifiTx() {
		return total_wifiTx;
	}

	public void setTotal_wifiTx(long total_wifiTx) {
		this.total_wifiTx = total_wifiTx;
	}

	@Column(name = "timestamp", columnDefinition = "DATETIME")
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
