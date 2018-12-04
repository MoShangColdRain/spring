package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UserDao;
import com.example.demo.model.User;
import com.example.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Override
	public List<User> getUserInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User selectUserByUserId(String userCode) {
		// TODO Auto-generated method stub
		return userDao.selectUserByUserId(userCode);
	}

}
