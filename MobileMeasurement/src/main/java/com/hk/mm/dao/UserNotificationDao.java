package com.hk.mm.dao;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.hk.mm.entity.UserNotifications;

@Transactional
public interface UserNotificationDao extends CrudRepository<UserNotifications, Long>{

}
