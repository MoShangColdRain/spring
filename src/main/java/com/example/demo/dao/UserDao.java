package com.example.demo.dao;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.User;

@Mapper
public interface UserDao {
	
	User getNameById(User user);
	
	User selectUserByUserId(String userId);
}
