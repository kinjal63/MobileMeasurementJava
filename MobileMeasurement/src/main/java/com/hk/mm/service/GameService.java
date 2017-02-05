package com.hk.mm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.mm.dao.UserGameResponseDao;
import com.hk.mm.vo.UserGameResponse;

@Service
public class GameService {

	@Autowired
	private UserGameResponseDao gameResponseDao;
	
	public List<UserGameResponse> getMutalGames()
	{
		return gameResponseDao.getMutalGames();
	}
}
