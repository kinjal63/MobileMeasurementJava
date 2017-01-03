package org.arpit.java2blog.dao;

import java.util.ArrayList;

import org.arpit.java2blog.model.AppData;
import org.arpit.java2blog.model.CallDuration;
import org.arpit.java2blog.model.GameData;
import org.arpit.java2blog.model.User;
import org.arpit.java2blog.model.UserAvailablity;
import org.arpit.java2blog.model.UserConnectionInfo;
import org.arpit.java2blog.model.UserDataUsage;
import org.arpit.java2blog.model.UserInput;
import org.arpit.java2blog.model.UserRSSI;

public interface UserDao {
	public int insert(User customer);
	public User findByEmail(String email);
	public void saveRssi(UserRSSI userRssi);
	public void saveDataUsage(UserDataUsage dataUsage);
	public void aggregateCallDuration(CallDuration callDuration);
	public void saveAppData(AppData appData);
	public void addGameInfo(GameData gameData);
	public void updateUserAvailablity(UserAvailablity availablity);
	Object getMutualGameList(long userId);
	Object getGameList();
	void editGame(GameData gameData);
	void deleteGame(long gameId);
	void sendConnectionInvite(UserConnectionInfo userConnectionInfo);
	void sendRemoteUserInput(UserInput userInput);
	Object getMutualGames(long userId, ArrayList<Long> userIds);
	void addUserAvailabilityTime(long userId, String fromTime, String toTime);
	void updateAndNotifyNearByUsers();
}