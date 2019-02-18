package com.example.demo.controller;

import com.example.demo.model.param.DateDto;
import com.example.demo.service.TaskTestService;
import com.example.demo.service.UploadDataService;
import com.example.demo.util.DateUtil;
import com.example.demo.util.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController  
@RequestMapping("/download")
public class DownloadController {

	private final static Logger logger = LoggerFactory.getLogger(DownloadController.class);
	@Autowired
	private UploadDataService uploadDataService;
	@Autowired
	private TaskTestService taskTestService;
	
	@RequestMapping(value="/downloadCSV",method=RequestMethod.GET)
	public void getContractDetailPdfFile(@RequestParam(value = "beginDate",required = true) String  beginDate,
										 @RequestParam(value = "endDate",required = true) String  endDate, HttpServletRequest request , HttpServletResponse response) throws IOException {
		try {
			// 取得输出流
			OutputStream out = response.getOutputStream();
			response.reset();// 清空输出流
			// 设定输出文件头
			response.setHeader("Content-disposition", "attachment; filename=" + toUtf8String("sss任务完成率统计表2", request) + ".xlsx");
			// 定义输出类型
			response.setContentType("application/msexcel");
			List<List<String>> result = taskTestService.exportData(beginDate, endDate);
			String fileName = ("/test1.xlsx");
			String date = "";
			String userAcc = "";
			ExcelUtil.fillData(result, fileName, date, userAcc, 1, 0, out);
		} catch (Exception e) {
			logger.debug("导出订单失败 原因：", e);
			logger.error("导出订单失败 原因：" + e);
		}
	}

	@RequestMapping(value="/repairByDate",method=RequestMethod.GET)
	public String repairByDate(@RequestParam(value = "beginDate", name = "开始时间 格式：2018-05-15") String beginDate,
									   @RequestParam(value = "endDate",name = "结束时间 格式：2018-05-15") String endDate) {

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

	private  String toUtf8String(String fileName, HttpServletRequest request) throws Exception {
		final String userAgent = request.getHeader("USER-AGENT");
		String finalFileName = null;
		if (StringUtils.contains(userAgent, "MSIE")) {// IE浏览器
			finalFileName = URLEncoder.encode(fileName, "UTF8");
		} else if (StringUtils.contains(userAgent, "Mozilla")) {// google,火狐浏览器
			finalFileName = new String(fileName.getBytes(), "ISO8859-1");
		} else {
			finalFileName = URLEncoder.encode(fileName, "UTF8");// 其他浏览器
		}
		return finalFileName;
	}
}
