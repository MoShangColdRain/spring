package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.TestShopTarget;
import com.example.demo.model.param.ImportDataResult;
import com.example.demo.service.UploadDataService;
import com.example.demo.service.UserService;

@RestController  
@RequestMapping("/fileUpload")  
public class FileController {

	private final static Logger logger = LoggerFactory.getLogger(FileController.class);	
	@Autowired
	private UploadDataService uploadDataService;
	
	@RequestMapping(value="/uploadShopTarget",method=RequestMethod.POST)
    public String uploadShopTarget(@RequestParam("file") MultipartFile file) {
		ImportDataResult importDataResult = uploadDataService.analyzeExcel(file, 6);
		
		List<TestShopTarget> testShopTargetList = new ArrayList<TestShopTarget>();
		if (importDataResult != null) {
			List<List<String>> result = importDataResult.getResult();
			if (result != null && result.size() > 0) {
				for (List<String> list : result) {
					TestShopTarget testShopTarget = new TestShopTarget();
					if (list != null && list.size() > 0) {
						for (int i = 0; i < list.size(); i++) {
							if (i == 0) {
								testShopTarget.setDate(list.get(i));
							}
							if (i == 1) {
								testShopTarget.setShopCode(list.get(i));
							}
							if (i == 2) {
								testShopTarget.setValidNewCustomerTargetNum(parseInt(list.get(i)));
							}
							if (i == 3) {
								testShopTarget.setIntoStoreTargetNum(parseInt(list.get(i)));
							}
							if (i == 4) {
								testShopTarget.setOrderTargetNum(parseInt(list.get(i)));
							}
							if (i == 5) {
								testShopTarget.setDeliveryTargetNum(parseInt(list.get(i)));
							}
						}
						testShopTargetList.add(testShopTarget);
					}
				}
			}
		}
		if (testShopTargetList.size() > 0) {
			uploadDataService.batchSave(testShopTargetList);
		}
        return "上传成功";
	}
	
	 private int parseInt(String str) {
	        if (StringUtils.isEmpty(str)) {
	            return 0;
	        }
	        return Integer.parseInt(str.trim());
	    }
}
