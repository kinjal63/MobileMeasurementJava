package org.arpit.java2blog.model;

public class Aggregate {
	private long userId;
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	private long totalMobileTx;
	private long totalMobileRx;
	private long totalWifiTx;
	private long totalWifiRx;
	
	public long getTotalMobileTx() {
		return totalMobileTx;
	}
	public long getTotalMobileRx() {
		return totalMobileRx;
	}
	public long getTotalWifiTx() {
		return totalWifiTx;
	}
	public long getTotalWifiRx() {
		return totalWifiRx;
	}
	public void setTotalMobileTx(long totalMobileTx) {
		this.totalMobileTx = totalMobileTx;
	}
	public void setTotalMobileRx(long totalMobileRx) {
		this.totalMobileRx = totalMobileRx;
	}
	public void setTotalWifiTx(long totalWifiTx) {
		this.totalWifiTx = totalWifiTx;
	}
	public void setTotalWifiRx(long totalWifiRx) {
		this.totalWifiRx = totalWifiRx;
	}

}
