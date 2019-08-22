package com.example.demo.service.impl;

import com.example.demo.dao.UserDao;
import com.example.demo.enums.ExchangeEnum;
import com.example.demo.model.User;
import com.example.demo.service.QueueMessageService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private QueueMessageService queueMessageService;

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

	@Override
	public void save(User user) throws Exception{

		userDao.save(user);
		queueMessageService.send(user.getUserCode(), ExchangeEnum.USER_REGISTER_TOPIC_EXCHANGE, null);

	}
}
