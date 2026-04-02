package com.zeroone.star.doc.controller;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 *描述 ：
 *Author:kunge
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @ApiModelProperty("获取用户信息")
    @GetMapping("/get")
    public String getUser(){
        return "电风扇地方";
    }
}
