package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

	@RequestMapping("/index")
    public String index(){
   //     map.put("name", "HelloController");
        String name = "test";
        String a;
        if ("test".equals(name)){
            a = "w";
        }
        return "index";
    }

    @RequestMapping("/seriesTarget")
    public String seriesTarget(){
        //     map.put("name", "HelloController");
        return "seriesTarget";
    }

    @RequestMapping("/video")
    public String video(){
        //     map.put("name", "HelloController");
        return "seriesTarget";
    }

}
