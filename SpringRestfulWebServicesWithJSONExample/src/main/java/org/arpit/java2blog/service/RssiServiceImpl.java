package org.arpit.java2blog.service;

import java.util.ArrayList;

import org.arpit.java2blog.dao.UserDao;
import org.arpit.java2blog.model.AppData;
import org.arpit.java2blog.model.CallDuration;
import org.arpit.java2blog.model.GameData;
import org.arpit.java2blog.model.User;
import org.arpit.java2blog.model.UserAvailablity;
import org.arpit.java2blog.model.UserConnectionInfo;
import org.arpit.java2blog.model.UserDataUsage;
import org.arpit.java2blog.model.UserInput;
import org.arpit.java2blog.model.UserRSSI;
import org.springframework.beans.factory.annotation.Autowired;

public class RssiServiceImpl implements RssiService {

	@Autowired
	UserDao userDao;
	
	public void saveRssi(UserRSSI rssi) {
		userDao.saveRssi(rssi);
	}

	public int insert(User customer) {
		return userDao.insert(customer);
	}

	public User findByEmail(String email) {
		return userDao.findByEmail(email);
	}

	@Override
	public void saveDataUsage(UserDataUsage dataUsage) {
		userDao.saveDataUsage(dataUsage);
	}
	
	@Override
	public void aggregateCallDuration(CallDuration callDuration) {
		userDao.aggregateCallDuration(callDuration);
	}

	@Override
	public void saveAppData(AppData appData) {
		userDao.saveAppData(appData);
	}
	
	public void addGameInfo(GameData gameData) {
		userDao.addGameInfo(gameData);
	};
	
	@Override
	public void updateUserAvailablity(UserAvailablity availablity) {
		userDao.updateUserAvailablity(availablity);
		
	}
	
	@Override
	public Object getMutualGameList(long userId) {
		return userDao.getMutualGameList(userId);
	}
	
	@Override
	public Object getGameList() {
		return userDao.getGameList();	
	}

	@Override
	public void editGame(GameData gameData) {
		userDao.editGame(gameData);
	}
	
	@Override
	public void deleteGame(long gameId) {
		userDao.deleteGame(gameId);
	}
	
	public void sendConnectionInvite(UserConnectionInfo userConnectionInfo) {
		userDao.sendConnectionInvite(userConnectionInfo);
	}
	
	public void sendRemoteUserInput(UserInput userInput) {
		userDao.sendRemoteUserInput(userInput);
	}
	
	@Override
	public Object getMutualGameList(long userId, ArrayList<Long> userIds) {
		return userDao.getMutualGames(userId, userIds);
	}
	
}
