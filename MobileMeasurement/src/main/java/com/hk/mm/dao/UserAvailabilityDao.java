package com.hk.mm.dao;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.hk.mm.entity.User;
import com.hk.mm.entity.UserAvailability;

@Transactional
public interface UserAvailabilityDao extends CrudRepository<UserAvailability, Long>{

	public UserAvailability findByUser(User user);
}
