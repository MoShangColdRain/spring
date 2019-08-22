package com.example.demo.controller;

import com.example.demo.model.param.DateDto;
import com.example.demo.service.TaskTestService;
import com.example.demo.service.UploadDataService;
import com.example.demo.util.DateUtil;
import com.example.demo.util.ExcelUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RestController  
@RequestMapping("/download")
public class DownloadController {


	final private float A4_weight = 595-60; //标准A4的宽
	final private float A4_height = 842-60; //标准A4的高

	private final static Logger logger = LoggerFactory.getLogger(DownloadController.class);
	@Autowired
	private UploadDataService uploadDataService;
	@Autowired
	private TaskTestService taskTestService;
	
	@RequestMapping(value="/downloadExcel",method=RequestMethod.GET)
	public void getContractDetailPdfFile(@RequestParam(value = "beginDate",required = true) String  beginDate,
										 @RequestParam(value = "endDate",required = true) String  endDate, HttpServletRequest request , HttpServletResponse response) throws IOException {
		try {
			// 取得输出流
			OutputStream out = response.getOutputStream();
			response.reset();// 清空输出流
			// 设定输出文件头
			response.setHeader("Content-disposition", "attachment; filename=" + toUtf8String("英菲任务完成率统计表2", request) + ".xlsx");
			// 定义输出类型
			response.setContentType("application/msexcel");
			List<List<String>> result = taskTestService.exportData(beginDate, endDate);
			String fileName = ("templates/test1.xlsx");
			String date = "";
			String userAcc = "";
			ExcelUtil.fillData(result, fileName, date, userAcc, 1, 0, out);
		} catch (Exception e) {
			logger.debug("导出订单失败 原因：", e);
			logger.error("导出订单失败 原因：" + e);
		}
	}

	@RequestMapping(value="/repairByDate",method=RequestMethod.GET)
	public String repairByDate(@RequestParam(value = "beginDate") String beginDate,
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

	@RequestMapping(value="/downloadCSV",method=RequestMethod.GET)
	public void getContractDetailPdfFile2(@RequestParam(value = "beginDate", required = true) String beginDate,
			@RequestParam(value = "endDate", required = true) String endDate, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		OutputStream outputStream = null;
        InputStream is = null;
        try {
        	is = taskTestService.getInputStream(beginDate, endDate);
            response.setHeader("content-disposition", "attachment;filename=" + toUtf8String("英菲任务完成率统计表", request) + ".csv" );
            //设置CSV三字节，使其可以使用UTF-8编码
            byte[] bom ={(byte) 0xEF,(byte) 0xBB,(byte) 0xBF};
            outputStream = response.getOutputStream();
            outputStream.write(bom);
            int temp = 0;
            while((temp = is.read()) != -1){
                outputStream.write(temp);
            }

        } catch (Exception e) {
            logger.error("导出数据出现异常", e);
        } finally {
            try {
                if (null != outputStream) {
                    outputStream.flush();
                    outputStream.close();
                }
                if(null != is)is.close();
            } catch (Exception e) {
                logger.error("Exception is ", e);
            }
        }
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

	@RequestMapping(value="/followRate",method=RequestMethod.GET)
	public String followRate(@RequestParam(value = "beginDate") String beginDate,
							   @RequestParam(value = "endDate") String endDate) {

		long beginTime = System.currentTimeMillis();
		List<DateDto> dateList = DateUtil.getDateList(beginDate, endDate);
		if (dateList != null && !dateList.isEmpty()){
			for (DateDto dateDto : dateList) {
				taskTestService.followRate(dateDto.getBeginDate(), dateDto.getNextDate());
			}
		}
		logger.info("task任务完成率统计完成，耗时[{}]ms", System.currentTimeMillis() - beginTime);
		return "";
	}

	@RequestMapping(value="/downFollowRate",method=RequestMethod.GET)
	public void downFollowRate(@RequestParam(value = "beginDate",required = true) String  beginDate,
										 @RequestParam(value = "endDate",required = true) String  endDate, HttpServletRequest request , HttpServletResponse response) throws IOException {
		try {
			// 取得输出流
			OutputStream out = response.getOutputStream();
			response.reset();// 清空输出流
			// 设定输出文件头
			response.setHeader("Content-disposition", "attachment; filename=" + toUtf8String("跟进完成率统计表", request) + ".xlsx");
			// 定义输出类型
			response.setContentType("application/msexcel");
			List<List<String>> result = taskTestService.downloadFollowRate(beginDate, endDate);
			String fileName = ("templates/test1.xlsx");
			String date = "";
			String userAcc = "";
			ExcelUtil.fillData(result, fileName, date, userAcc, 1, 0, out);
		} catch (Exception e) {
			logger.debug("导出订单失败 原因：", e);
			logger.error("导出订单失败 原因：" + e);
		}
	}

	@RequestMapping(value="/pdf",method=RequestMethod.GET)
	public void downFollowRate(HttpServletRequest request , HttpServletResponse response){
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			response.setHeader("Content-disposition", "attachment;filename=" + toUtf8String("盖伦", request) + ".pdf");
			// 定义输出类型
			response.setContentType("application/pdf");

			Document doc = new Document(PageSize.A4,30,30,30,30);
			PdfWriter.getInstance(doc, os);
			BufferedImage img = null;
			List<String> list = new ArrayList<>();
			list.add("https://img.souche.com/files/default/sdochf5vz92crk3yeafcjwg25fpv2zih.jpg");
			list.add("http://souche-devqa.oss-cn-hangzhou.aliyuncs.com/20171208/jpg/f3d6437d68c6be79afbb2b3561683960.jpg");

			// 打开容器
			doc.open();
			float percent=100;
			float w,h;
			for (String imageUrl : list){
				// 实例化图片
				URL url = new URL(imageUrl);
				img = ImageIO.read(url);
				Image image = Image.getInstance(url);
		//		image.setAlignment(Image.ALIGN_CENTER);
				/*处理图片缩放比例*/
				w = img.getWidth();
				h = img.getHeight();
				if((w > A4_weight)&&(h < A4_height)) {
					percent = (A4_weight*100)/w ;

				} else if((w < A4_weight)&&(h > A4_height)) {
					percent = (A4_height*100)/h;
				} else if((w > A4_weight)&&(h > A4_height)){
					percent = (A4_weight*100)/w ;
					h = (h*percent)/100;
					if(h > A4_height) {
						percent = (A4_height*100)/h;
					}
				}

				image.scalePercent(percent);
			//	doc.setPageSize(new Rectangle(img.getWidth(), img.getHeight()));

				doc.add(image);
			}

			doc.close();

			ByteArrayInputStream bis =  new ByteArrayInputStream(doc.toString().getBytes("utf-8"));

			int i=0;
			byte[] buffer = new byte[1024];

			while((i=bis.read(buffer))!=-1) {
				os.write(buffer, 0, i);
			}
			os.flush();
		} catch (Exception e) {
		} finally {
			try {
				os.close();
			} catch (Exception e2){
			}

		}
	}

}
