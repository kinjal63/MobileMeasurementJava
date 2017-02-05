package com.hk.mm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.mm.dao.UserAvailabilityDao;
import com.hk.mm.dao.UserAvailabilityTimeDao;
import com.hk.mm.dao.UserDao;
import com.hk.mm.dao.UserNotificationDao;
import com.hk.mm.entity.User;
import com.hk.mm.entity.UserAvailability;
import com.hk.mm.entity.UserAvaillableTimes;
import com.hk.mm.entity.UserNotifications;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserAvailabilityTimeDao availabilityTimeDao;

	@Autowired
	private UserNotificationDao notificationDao;
	
	@Autowired
	private UserAvailabilityDao userAvailabilityDao;

	public User findByEmail(final String email) {
		return userDao.findByEmail(email);
	}

	public List<User> findAll() {
		return (List<User>) userDao.findAll();
	}

	public User findByUserId(final long userId) {
		return userDao.findOne(userId);
	}

	public void addUserTime(final UserAvaillableTimes availlableTimes) {
		availabilityTimeDao.save(availlableTimes);
	}
	
	public UserAvailability findUserAvailable(final User user)
	{
		return userAvailabilityDao.findByUser(user);
	}

	public void addUserNotification(final UserNotifications userNotifications) {
		notificationDao.save(userNotifications);
	}
	
}
