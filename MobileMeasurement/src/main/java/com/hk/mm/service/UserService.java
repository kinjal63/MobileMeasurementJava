package com.hk.mm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.mm.dao.UserDao;
import com.hk.mm.entity.User;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	public User findByEmail(String email)
	{
		return userDao.findByEmail(email);
	}
	
	public List<User> findAll()
	{
		return (List<User>) userDao.findAll();
	}
}
