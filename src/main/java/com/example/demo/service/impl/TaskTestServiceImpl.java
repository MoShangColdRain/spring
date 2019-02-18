package com.example.demo.service.impl;

import com.example.demo.dao.TaskAnalysisDao;
import com.example.demo.dao.TaskTestDao;
import com.example.demo.model.TaskAnalysis;
import com.example.demo.service.TaskTestService;
import com.example.demo.util.DateTestUtil;
import com.example.demo.util.DateUtil;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: lx
 * @create: 2019-02-18
 **/
public class TaskTestServiceImpl implements TaskTestService {


    private static final String ENCODING = "UTF-8";

    @Autowired
    private TaskTestDao taskTestDao;

    @Autowired
    private TaskAnalysisDao taskAnalysisDao;

    @Override
    public void test(Date day) {
        int list1 =  taskTestDao.getPassTodayTotalTaskCount(new Date(DateTestUtil.endOfDay(day)), new Date(DateTestUtil.startOfTomorrow(day)));
        int list2 =  taskTestDao.getTodayUnDealTotalTaskCount(new Date(DateTestUtil.endOfDay(day)));
        int list3 =  taskTestDao.getTodayDealTotalTaskCount(new Date(DateTestUtil.startOfDay(day)),new Date(DateTestUtil.endOfDay(day)));
        int list4 =  taskTestDao.getPassRemoveTotalTaskCount(new Date(DateTestUtil.endOfDay(day)));

        TaskAnalysis taskAnalysis = new TaskAnalysis();
        taskAnalysis.setPassTodayCount(list1);
        taskAnalysis.setTodayUnDealCount(list2);
        taskAnalysis.setTodayDealCount(list3);
        taskAnalysis.setPassRemoveCount(list4);
        int total = list1 + list2 + list3 + list4;
        taskAnalysis.setTotal(total);

        double percent = 0;
        if (total > 0) {
            BigDecimal bg = new BigDecimal((double)list3 / total).setScale(4, RoundingMode.HALF_UP);

            percent = bg.doubleValue();
        }
        taskAnalysis.setPercent(percent);
        taskAnalysis.setDate(day);
        taskAnalysisDao.insert(taskAnalysis);
    }

    @SuppressWarnings("resource")
    @Override
    public void fillData(List<List<String>> result, String fileName, String date, String creator, int startRow, int startCell, OutputStream out) {
        try {
            InputStream is = TaskTestServiceImpl.class.getClassLoader().getResourceAsStream(fileName);
            XSSFWorkbook wb = new XSSFWorkbook(is);
            XSSFSheet sheet = wb.getSheetAt(0);
            int nameCount = wb.getNumberOfNames();
            for (int i = 0; i < nameCount; i++) {
                XSSFName name = wb.getNameAt(i);
                if ("CreateDate".equals(name.getNameName())) {
                    // System.out.println();
                }
                if ("Creator".equals(name.getNameName())) {

                }
            }
            for (List<String> datas : result) {
                int firstCell = startCell;
                //获取单元格
                XSSFRow row = sheet.createRow(startRow);
                for (String data : datas) {
                    XSSFCell cell = row.createCell(firstCell);
                    //写入单元格内容
                    cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(data);
                    firstCell++;
                }
                startRow++;
            }
            wb.write(out);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public List<List<String>> exportData(String startDate, String endDate) {
        List<TaskAnalysis> taskAnalysisList = taskAnalysisDao.queryTask(startDate, endDate);
        List<List<String>> result = new ArrayList<>();
        List<String> list;
        for (TaskAnalysis taskAnalysis : taskAnalysisList) {
            list = new ArrayList<>();
            list.add(taskAnalysis.getTodayDealCount().toString());
            list.add(taskAnalysis.getTotal().toString());
            list.add(taskAnalysis.getPercent().toString());
            list.add(DateUtil.dateFmt("yyyy-MM-dd", taskAnalysis.getDate()));
            result.add(list);

        }
        return result;
    }

    @Override
    public InputStream getInputStream(String startDate, String endDate) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
//	        final CSVData<T> csvData = getCSVData();
        String header = "今日完成任务数,今日总任务,任务完成率,日期";
        sb.append(header);
        sb.append("\n");

        StringBuffer stringBuffer = exportData2(startDate, endDate);
        sb.append(stringBuffer);

        return new ByteArrayInputStream(sb.toString().getBytes(ENCODING));
    }


    @Override
    public StringBuffer exportData2(String startDate, String endDate) {
        List<TaskAnalysis> taskAnalysisList = taskAnalysisDao.queryTask(startDate, endDate);
        List<List<String>> result = new ArrayList<>();
        List<String> list;
        StringBuffer stringBuffer = new StringBuffer();

        for (TaskAnalysis taskAnalysis : taskAnalysisList) {
            stringBuffer.append(taskAnalysis.getTodayDealCount().toString()).append(",");
            stringBuffer.append(taskAnalysis.getTotal().toString()).append(",");
            stringBuffer.append(taskAnalysis.getPercent().toString()).append(",");
            stringBuffer.append(DateUtil.dateFmt("yyyy-MM-dd", taskAnalysis.getDate())).append("\n");

        }
        return stringBuffer;
    }
}
