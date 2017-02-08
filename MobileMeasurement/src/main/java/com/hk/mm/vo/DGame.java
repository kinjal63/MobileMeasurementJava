package com.hk.mm.vo;

public class DGame {

	private String message;
	private String userId;
	private String gameName;
	private String gamePackageName;
	private long gameId;
	private String gameImagePath;
	private int gameNetworkType;

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGamePackageName() {
		return gamePackageName;
	}

	public void setGamePackageName(String gamePackageName) {
		this.gamePackageName = gamePackageName;
	}

	public long getGameId() {
		return gameId;
	}

	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

	public String getGameImagePath() {
		return gameImagePath;
	}

	public void setGameImagePath(String gameImagePath) {
		this.gameImagePath = gameImagePath;
	}

	public int getGameNetworkType() {
		return gameNetworkType;
	}

	public void setGameNetworkType(int gameNetworkType) {
		this.gameNetworkType = gameNetworkType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
