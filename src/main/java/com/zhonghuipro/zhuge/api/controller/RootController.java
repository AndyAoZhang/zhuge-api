package com.zhonghuipro.zhuge.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    @RequestMapping("/")
    public String rootRequest(){
        return "Hello, 钟辉教编程";
    }
}
