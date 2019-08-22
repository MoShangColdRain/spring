package com.example.demo.controller;

import com.example.demo.model.param.DateDto;
import com.example.demo.service.TaskTestService;
import com.example.demo.service.UploadDataService;
import com.example.demo.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController  
@RequestMapping("/InfinitiCustomerController")
public class InfinitiCustomerController {

	private final static Logger logger = LoggerFactory.getLogger(InfinitiCustomerController.class);
	@Autowired
	private UploadDataService uploadDataService;
	@Autowired
	private TaskTestService taskTestService;

	@RequestMapping(value="/repairByDate",method=RequestMethod.GET)
	public String getFollowRate(@RequestParam(value = "beginDate") String beginDate,
									   @RequestParam(value = "endDate") String endDate) {
		long beginTime = System.currentTimeMillis();
		List<DateDto> dateList = DateUtil.getDateList(beginDate, endDate);
		if (dateList != null && !dateList.isEmpty()){
			for (DateDto dateDto : dateList) {
				taskTestService.test(dateDto.getBeginDate());
			}
		}
		logger.info("task任务完成率统计完成，耗时[{}]ms", System.currentTimeMillis() - beginTime);
		return "";
	}
}
