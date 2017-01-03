package org.arpit.java2blog.schedule;

import org.arpit.java2blog.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class ScheduleService {
	
	@Autowired
	private UserDao userDao;
	
	    @Scheduled(fixedDelay = 20000)
	    public void notificationServiceMethod()
	    {
	        userDao.updateAndNotifyNearByUsers();
	    }
}
