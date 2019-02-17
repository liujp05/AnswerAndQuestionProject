package com.jpliu.Project.controller;

import com.jpliu.Project.service.WendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class SettingController {

    @Autowired
    WendaService wendaService;//会将定义在service里面的wendaService注入进来 初始化

    @RequestMapping(path = {"/setting"}, method = RequestMethod.GET) //访问这样的地址 就是返回这个字符串
    @ResponseBody //表示返回的不是模板
    public String setting(HttpSession httpSession) {
        return "Setting OK";
    }
}
