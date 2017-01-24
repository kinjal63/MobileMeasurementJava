package com.hk.mm.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "game_library")
public class GameLibrary {

	private long gameLibId;
	private String gameName;
	private String gamePublisherName;
	private String gamePkgName;
	private String gameStudioName;
	private String gameImgPath;
	private int ageRating;
	private int supportingOS;
	private int networkType;
	private int minPlayers;
	private int maxPlayers;
	private Date timestamp;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public long getGameLibId() {
		return gameLibId;
	}

	public void setGameLibId(long gameLibId) {
		this.gameLibId = gameLibId;
	}

	@Column(name = "game_name")
	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	@Column(name = "game_publisher_name")
	public String getGamePublisherName() {
		return gamePublisherName;
	}

	public void setGamePublisherName(String gamePublisherName) {
		this.gamePublisherName = gamePublisherName;
	}

	@Column(name = "game_package_name")
	public String getGamePkgName() {
		return gamePkgName;
	}

	public void setGamePkgName(String gamePkgName) {
		this.gamePkgName = gamePkgName;
	}

	@Column(name = "game_studio_name")
	public String getGameStudioName() {
		return gameStudioName;
	}

	public void setGameStudioName(String gameStudioName) {
		this.gameStudioName = gameStudioName;
	}

	@Column(name = "game_image_path")
	public String getGameImgPath() {
		return gameImgPath;
	}

	public void setGameImgPath(String gameImgPath) {
		this.gameImgPath = gameImgPath;
	}

	@Column(name = "age_rating")
	public int getAgeRating() {
		return ageRating;
	}

	public void setAgeRating(int ageRating) {
		this.ageRating = ageRating;
	}

	@Column(name = "supporting_os")
	public int getSupportingOS() {
		return supportingOS;
	}

	public void setSupportingOS(int supportingOS) {
		this.supportingOS = supportingOS;
	}

	@Column(name = "network_type")
	public int getNetworkType() {
		return networkType;
	}

	public void setNetworkType(int networkType) {
		this.networkType = networkType;
	}

	@Column(name = "min_players")
	public int getMinPlayers() {
		return minPlayers;
	}

	public void setMinPlayers(int minPlayers) {
		this.minPlayers = minPlayers;
	}

	@Column(name = "max_players")
	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	@Column(name = "created_date", columnDefinition = "DATETIME")
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
