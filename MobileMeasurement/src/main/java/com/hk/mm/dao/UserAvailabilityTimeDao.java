package com.hk.mm.dao;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.hk.mm.entity.UserAvaillableTimes;

@Transactional
public interface UserAvailabilityTimeDao extends CrudRepository<UserAvaillableTimes, Long>{

}
