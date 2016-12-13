package org.arpit.java2blog.service;

import org.arpit.java2blog.model.AppData;
import org.arpit.java2blog.model.CallDuration;
import org.arpit.java2blog.model.GameData;
import org.arpit.java2blog.model.User;
import org.arpit.java2blog.model.UserAvailablity;
import org.arpit.java2blog.model.UserDataUsage;
import org.arpit.java2blog.model.UserRSSI;

public interface RssiService {
	public int insert(User customer);
	public User findByEmail(String email);
	void saveRssi(UserRSSI rssi);
	void saveDataUsage(UserDataUsage dataUsage);
	void aggregateCallDuration(CallDuration callDuration);
	void saveAppData(AppData appData);
	void addGameInfo(GameData gameData);
	void updateUserAvailablity(UserAvailablity availablity);
	Object getMutualGameList(long userId);
	Object getGameList();
	void editGame(GameData gameData);
	void deleteGame(long gameId);
}
