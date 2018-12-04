package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.example.demo.model.User;
import com.example.demo.service.EmailService;
import com.example.demo.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "customerExts",description = "客户扩展")
@RestController  
@RequestMapping("/user")  
public class UserController {

	private final static Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
	
    @ApiOperation(value = "新增用户" ,  notes="新增注册")
	@RequestMapping(value="/getUserInfo",method=RequestMethod.GET)
    public User getUserInfo(String userCode) {
		User user = userService.selectUserByUserId(userCode);
		logger.info(JSON.toJSONString(user));
        return user;
	}
	
	@RequestMapping(value="/hello", method=RequestMethod.GET)  
    public String index() { 
		String message = "<!DOCTYPE html>\n" + 
				"<html lang=\"zh\" xmlns:th=\"http://www.thymeleaf.org\">\n" + 
				"    <head>\n" + 
				"        <meta charset=\"UTF-8\"/>\n" + 
				"        <title>Title</title>\n" + 
				"    </head>\n" + 
				"    <body>\n" + 
				"        <h4 th:text=\"|尊敬的:${username} 用户：|\"></h4><br /><br />\n" + 
				"\n" + 
				"       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;恭喜您入住xxx平台VIP会员,您将享受xxx优惠福利，同时感谢您对xxx的关注与支持并欢迎您使用xx的产品与服务。<br />\n" + 
				"       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;...\n" + 
				"    </body>\n" + 
				"</html>";
		emailService.sendHtmlEmail("liuxiang@souche.com", "ss", message);
        return"Hello World";  
    }
}
