package com.example.demo.dao;

import com.example.demo.model.TaskAnalysis;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaskAnalysisDao {

    void insert(TaskAnalysis TaskAnalysis);

    List<TaskAnalysis> queryTask(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
