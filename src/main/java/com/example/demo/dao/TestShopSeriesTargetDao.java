package com.example.demo.dao;

import com.example.demo.model.TestShopSeriesTarget;
import com.example.demo.model.TestShopTarget;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TestShopSeriesTargetDao {
    int deleteByPrimaryKey(Integer id);

    int insert(TestShopSeriesTarget record);

    int insertSelective(TestShopSeriesTarget record);

    TestShopSeriesTarget selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TestShopSeriesTarget record);

    int updateByPrimaryKey(TestShopSeriesTarget record);

    void batchSave(List<TestShopSeriesTarget> testShopSeriesTargetList);

}