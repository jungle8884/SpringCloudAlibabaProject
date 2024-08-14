package com.test.controller;

import com.test.entity.User;
import com.test.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RefreshScope //添加此注解就能实现自动刷新了
public class UserController {

    @Resource
    UserService service;

    @Value("${test.txt}")
    String testString;

    @RequestMapping("/user/{uid}")
    public User findUserById(@PathVariable("uid") int uid){
        System.out.println(testString + ": User-" + Thread.currentThread().getName() + "被调用");
        return service.getUserById(uid);
    }
}
