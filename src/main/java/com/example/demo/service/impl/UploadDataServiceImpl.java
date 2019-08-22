package com.example.demo.service.impl;

import com.example.demo.dao.TestShopSeriesTargetDao;
import com.example.demo.dao.TestShopTargetDao;
import com.example.demo.model.TestShopSeriesTarget;
import com.example.demo.model.TestShopTarget;
import com.example.demo.model.param.ImportDataResult;
import com.example.demo.service.UploadDataService;
import com.example.demo.util.DateUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UploadDataServiceImpl implements UploadDataService {

    private static final Logger logger = LoggerFactory.getLogger(UploadDataService.class);
    
    @Autowired
    private TestShopTargetDao testShopTargetDao;


    @Autowired
    private TestShopSeriesTargetDao testShopSeriesTargetDao;

    private static int successCount = 0;
    private static int notFindCount = 0;
    private static int seriesNotFindCount = 0;
    private static int alreadyExistCount = 0;
    private static int errorCount = 0;
    private static int validateFailCount = 0;

    @Override
    public ImportDataResult analyzeExcel(MultipartFile file, int length) {
        successCount = 0;
        notFindCount = 0;
        seriesNotFindCount = 0;
        alreadyExistCount = 0;
        errorCount = 0;
        validateFailCount = 0;
        int total = 0;


        Workbook wb = null;
        ImportDataResult importDataResult = new ImportDataResult();

        try {
            //根据文件格式(2003或者2007)来初始化
            if (file.getOriginalFilename().endsWith("xlsx")) {
                wb = new XSSFWorkbook(file.getInputStream());
            } else {
                wb = new HSSFWorkbook(file.getInputStream());
            }
            //获得第一个表单
            Sheet sheet = wb.getSheetAt(0);
            //格式化 number String
            DecimalFormat df = new DecimalFormat("0");
            int sheetNumber = sheet.getLastRowNum() + 1;
            logger.info("sheet sheetNumber is ... [{}]",sheetNumber);
            int pageTotol = (sheetNumber%3000==0?sheetNumber/3000:sheetNumber/3000+1);
            int currentPage = 1;
            List<List<String>> testAnalysisMains = new ArrayList<>();
            for (int rowIndex = 0; rowIndex < sheetNumber; rowIndex++) {  //getLastRowNum,获取最后一行的行标
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }

                List<String> strList = new ArrayList<>(length);
                for (int cellIndex = 0; cellIndex < length; cellIndex++) {    //getLastCellNum, 获取最后一个不为空的列
                    Cell cell = row.getCell(cellIndex);
                    if (row.getCell(cellIndex) != null) {   //getCell 获取单元格数据
                        String str = null;
                        switch (cell.getCellType()) {   //根据cell中的类型来输出数据
                            case HSSFCell.CELL_TYPE_NUMERIC:
                                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                                    Date d = cell.getDateCellValue();
                                    str = DateUtil.dateFmt(DateUtil.DEFAULT_YEAR_MONTH_PATTERN, d);
                                } else {
                                    str = df.format(cell.getNumericCellValue()).toString();
                                }
                                break;
                            case HSSFCell.CELL_TYPE_STRING:
                                str = cell.getStringCellValue();
                                break;
                            case HSSFCell.CELL_TYPE_BLANK:
                                str = "";
                                break;
                            default:
                                str = cell.toString();
                                break;
                        }
                        strList.add(str);
                    } else {
                        strList.add("");
                    }
                }
//                TestAnalysisMain testAnalysisMain = new TestAnalysisMain();
//                int listSize = strList.size();//判断列数量
//                if (listSize > 0)
//                    testAnalysisMain.setTransferDate(DateUtil.parseDate(trim(strList.get(0)), DateUtil.DEFAULT_DATE_PATTERN));
//                if (listSize > 1)
//                    testAnalysisMain.setShopCode(trim(strList.get(1)));
//                if (listSize > 2)
//                    testAnalysisMain.setSeriesCode(trim(strList.get(2)));
//                if (listSize > 3)
//                    testAnalysisMain.setFirstSourceCode(trim(strList.get(3)));
//                if (listSize > 4)
//                    testAnalysisMain.setClueNum(parseInt(strList.get(4)));
//                if (listSize > 5)
//                    testAnalysisMain.setValidNewCustomerNum(parseInt(strList.get(5)));
//                if (listSize > 6)
//                    testAnalysisMain.setIntoStoreNum(parseInt(strList.get(6)));
//                if (listSize > 7)
//                    testAnalysisMain.setFirstIntoStoreNum(parseInt(strList.get(7)));
//                if (listSize > 8)
//                    testAnalysisMain.setNotFirstIntoStoreNum(parseInt(strList.get(8)));
//                if (listSize > 9)
//                    testAnalysisMain.setTestDriveNum(parseInt(strList.get(9)));
//                if (listSize > 10)
//                    testAnalysisMain.setOrderNum(parseInt(strList.get(10)));
//                if (listSize > 11)
//                    testAnalysisMain.setDeliveryNum(parseInt(strList.get(11)));
                testAnalysisMains.add(strList);
                total++;
//                if (total == 3000 || (currentPage == pageTotol && rowIndex+1 == sheetNumber)) {
//                    total = 0;
//                    testAnalysisMains = upload(testAnalysisMains);
//                    currentPage ++;
//                }
                importDataResult.setTotal(total);
                importDataResult.setResult(testAnalysisMains);
            }

        } catch (IOException e) {
        	logger.error("读取excel文件出错了！！！", e);
        } finally {
            if (wb != null) {
                try {
                    ((InputStream) wb).close();
                } catch (Exception e2) {
                }
            }
        }
        return importDataResult;

    }

//    private List<TestAnalysisMain> upload(List<TestAnalysisMain> testAnalysisMainList) {
//        if (CollectionUtils.isEmpty(testAnalysisMainList))return new ArrayList<>();
//        logger.info("ClueExcelRowList size is [{}]", testAnalysisMainList.size());
//        try {
//            Integer counts = testAnalysisMainService.insert(testAnalysisMainList);
//            logger.info("insert counts is [{}]...size is [{}]",counts,testAnalysisMainList.size());
//            return new ArrayList<>();
//        } catch (Exception e) {
//        	logger.error("Exception in upload ... ", e);
//            errorCount++;
//            return new ArrayList<>();
//        }
//    }


    private String trim(String src) {
        if (StringUtils.isEmpty(src)) {
            return "";
        }
        return src.trim();
    }

    private int parseInt(String str) {
        if (StringUtils.isEmpty(str)) {
            return 0;
        }
        return Integer.parseInt(str.trim());
    }
    
    public void batchSave(List<TestShopTarget> testShopTargetList) {
    	testShopTargetDao.batchSave(testShopTargetList);
    }

    public void batchSeriesSave(List<TestShopSeriesTarget> testShopSeriesTargetList) {
        testShopSeriesTargetDao.batchSave(testShopSeriesTargetList);
    }

}
















