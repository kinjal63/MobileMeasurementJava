package com.hk.mm.dao;

import org.springframework.data.repository.CrudRepository;

import com.hk.mm.entity.User;
import com.hk.mm.entity.UserAvailability;

public interface UserAvailabilityDao extends CrudRepository<UserAvailability, Long>{

	public UserAvailability findByUser(User user);
}
