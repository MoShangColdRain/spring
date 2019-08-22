package com.example.demo.service;

import com.example.demo.model.TestShopSeriesTarget;
import com.example.demo.model.TestShopTarget;
import com.example.demo.model.param.ImportDataResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UploadDataService {

    public ImportDataResult analyzeExcel(MultipartFile file, int length);

    void batchSave(List<TestShopTarget> testShopTargetList);

    void batchSeriesSave(List<TestShopSeriesTarget> testShopSeriesTargetList);

}
