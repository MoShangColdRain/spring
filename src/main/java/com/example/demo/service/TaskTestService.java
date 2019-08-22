package com.example.demo.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

public interface TaskTestService {

    void test(Date day);

    List<List<String>> exportData(String startDate, String endDate);

    void fillData(List<List<String>> result, String fileName, String date, String creator, int startRow, int startCell, OutputStream out);

    InputStream getInputStream(String startDate, String endDate) throws UnsupportedEncodingException;

    StringBuffer exportData2(String startDate, String endDate);

    void followRate(Date startDate, Date endDate);

    List<List<String>> downloadFollowRate(String startDate, String endDate);

    }
