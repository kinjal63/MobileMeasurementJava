package com.hk.mm.dao;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.hk.mm.entity.User;

@Transactional
public interface UserDao extends CrudRepository<User, Long> {

	public User findByEmail(String email);

}
