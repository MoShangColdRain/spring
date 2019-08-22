package com.example.demo.dao;

import com.example.demo.model.FollowRate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface CustomerDao {

    Integer getCustomerSum(@Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

    Integer getFollowSum(@Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

    void insert(FollowRate followRate);

    List<FollowRate> queryFollowRate(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
