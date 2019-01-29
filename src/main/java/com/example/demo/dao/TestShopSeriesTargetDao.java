package com.example.demo.dao;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.TestShopSeriesTarget;

@Mapper
public interface TestShopSeriesTargetDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TestShopSeriesTarget record);

    int insertSelective(TestShopSeriesTarget record);

    TestShopSeriesTarget selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TestShopSeriesTarget record);

    int updateByPrimaryKey(TestShopSeriesTarget record);
}