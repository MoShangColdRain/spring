package com.example.demo.controller;

import com.example.demo.service.UserService;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: lx
 * @create: 2019-04-16
 **/
@Api(value = "test",description = "test")
@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    @Autowired
    private UserService userService;

    @ApiOperation(value = "test" ,  notes="新增注册")
    @RequestMapping(value="/repairByDate",method=RequestMethod.GET)
    public String getFollowRate() {

        logger.info("成功");
        return "测试成功";
    }

}
