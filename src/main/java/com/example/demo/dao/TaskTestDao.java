package com.example.demo.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface TaskTestDao {

    Integer getPassTodayTotalTaskCount(@Param("todayEnd") Date todayEnd, @Param("tomorrowStart") Date tomorrowStart);
    Integer getTodayUnDealTotalTaskCount(@Param("todayEnd") Date todayEnd);
    Integer getTodayDealTotalTaskCount(@Param("todayStart") Date todayStart, @Param("todayEnd") Date todayEnd);
    Integer getPassRemoveTotalTaskCount(@Param("todayEnd") Date todayEnd);


}
