package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.TestShopTarget;

@Mapper
public interface TestShopTargetDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TestShopTarget record);

    TestShopTarget selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TestShopTarget record);

    int updateByPrimaryKey(TestShopTarget record);
    
    void batchSave(List<TestShopTarget> testShopTargetList);
}