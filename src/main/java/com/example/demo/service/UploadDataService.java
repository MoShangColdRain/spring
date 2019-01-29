package com.example.demo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.TestShopTarget;
import com.example.demo.model.param.ImportDataResult;

public interface UploadDataService {

    public ImportDataResult analyzeExcel(MultipartFile file, int length);

    void batchSave(List<TestShopTarget> testShopTargetList);
}
