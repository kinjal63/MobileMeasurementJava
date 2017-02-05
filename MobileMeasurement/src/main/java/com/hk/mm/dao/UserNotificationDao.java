package com.hk.mm.dao;

import org.springframework.data.repository.CrudRepository;

import com.hk.mm.entity.UserNotifications;

public interface UserNotificationDao extends CrudRepository<UserNotifications, Long>{

}
