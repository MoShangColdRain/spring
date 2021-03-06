package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.demo.service.EmailService;
import com.example.demo.service.ShortMessageService;
import com.example.demo.util.VideoUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private EmailService emailService;
 
	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	private ShortMessageService shortMessageService;

	@Test
	public void contextLoads() {
	}

	@Test
	public void sendTemplateMail() {
		//创建邮件正文
		Context context = new Context();
		context.setVariable("user", "zoey");
		context.setVariable("web", "夏梦雪");
		context.setVariable("company", "测试公司");
		context.setVariable("product","梦想产品");
		String emailContent = templateEngine.process("email", context);
//		mailService.sendHtmlMail("xxxx@qq.com","主题：这是模板邮件",emailContent);		
		emailService.sendHtmlEmail("liuxiang@souche.com", "主题：这是模板邮件", emailContent);	
	}
	
	@Test
	public void sendTemplateMail11() {
		//创建邮件正文
		Context context = new Context();
		context.setVariable("user", "zoey");
		context.setVariable("web", "夏梦雪");
		context.setVariable("company", "测试公司");
		context.setVariable("product","梦想产品");
		String emailContent = templateEngine.process("email", context);
//		mailService.sendHtmlMail("xxxx@qq.com","主题：这是模板邮件",emailContent);		
		emailService.sendInlineResourceEmail("liuxiang@souche.com", "主题：这是模板邮件", emailContent,"/Users/liuxiang/Pictures/20180124192825.jpg", "sdsds");	
		
	}
	@Test
	public void sendTemplateMail1111() {
		String path ="http://***********/image/1532505427121.mp4";
		String host = "";
		String lpath ="";
		String vPath ="";
        try {
        	lpath = path.substring(path.lastIndexOf("/")+1,path.lastIndexOf("."))+".png";//自定义输出图片名截取与视频名一样
            vPath = VideoUtil.randomGrabberFFmpegVideoImage(path, lpath, VideoUtil.MOD);//(这是视频的第一帧是保存的服务器的路径)
        } catch (Exception e) {
            e.printStackTrace();
        }
        vPath = host+"/项目名/image/"+lpath;

	}
	
	
}
