package com.hk.mm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.mm.dao.GameLibraryDao;
import com.hk.mm.entity.GameLibrary;

@Service
public class GameService {

	// @Autowired
	// private UserGameResponseDao gameResponseDao;

	@Autowired
	private GameLibraryDao gameLibraryDao;

	// public List<UserGameResponse> getMutalGames()
	// {
	// return gameResponseDao.getMutalGames();
	// }

	public List<GameLibrary> findAll() {
		return (List<GameLibrary>) gameLibraryDao.findAll();
	}

	public void deleteGame(final GameLibrary gameLibrary) {
		gameLibraryDao.delete(gameLibrary);
	}
}
