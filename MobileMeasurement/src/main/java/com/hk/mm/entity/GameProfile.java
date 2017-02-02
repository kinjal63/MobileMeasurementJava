package com.hk.mm.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "game_profile")
public class GameProfile {

	private long gameProfileId;
	private User user;
	private GameLibrary gameLibrary;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public long getGameProfileId() {
		return gameProfileId;
	}

	public void setGameProfileId(long gameProfileId) {
		this.gameProfileId = gameProfileId;
	}

	@OneToOne(cascade = CascadeType.ALL)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@OneToOne(cascade = CascadeType.ALL)
	public GameLibrary getGameLibrary() {
		return gameLibrary;
	}

	public void setGameLibrary(GameLibrary gameLibrary) {
		this.gameLibrary = gameLibrary;
	}

}
