package com.test.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSONObject;
import com.test.entity.User;
import com.test.entity.UserBorrowDetail;
import com.test.service.BorrowService;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collections;

@RestController
@RefreshScope
public class BorrowController {

    @Resource
    BorrowService service;

    @RequestMapping("/borrow/{uid}")
    UserBorrowDetail findUserBorrows(@PathVariable("uid") int uid){
        System.out.println("Borrow" + Thread.currentThread().getName() + "被调用");
        return service.getUserBorrowDetailByUid(uid);
    }

    @RequestMapping("/borrow2/{uid}")
    UserBorrowDetail findUserBorrows2(@PathVariable("uid") int uid){
        return service.getUserBorrowDetailByUid(uid);
    }

    @RequestMapping("/borrow3/{uid}")
    String findUserBorrows3(@PathVariable("uid") int uid) throws InterruptedException{
        Thread.sleep(1000);
        return "熔断策略";
    }

    @RequestMapping("/borrow4/{uid}")
    @SentinelResource(value = "findUserBorrows4", blockHandler = "testBorrow4")
    UserBorrowDetail findUserBorrows4(@PathVariable("uid") int uid) {
        throw new RuntimeException();
    }

    UserBorrowDetail testBorrow4(int uid, BlockException e){
        return new UserBorrowDetail(new User(), Collections.emptyList());
    }

    // 限流页面
    @RequestMapping("/blocked")
    JSONObject blocked(){
        JSONObject object = new JSONObject();
        object.put("code", 403);
        object.put("success", false);
        object.put("massage", "您的请求频率过快，请稍后再试！");
        return object;
    }

    @RequestMapping("/test")
    @SentinelResource(value = "test",
            fallback = "except", //fallback指定出现异常时的替代方案
            blockHandler = "block", // 如果配置了`blockHandler`，那么在出现限流时，依然只会执行`blockHandler`指定的替代方案
            exceptionsToIgnore = IOException.class) //忽略那些异常，也就是说这些异常出现时不使用替代方案
    String test(){
        throw new RuntimeException("test");
    }

    //替代方法必须和原方法返回值和参数一致，最后可以添加一个Throwable作为参数接受异常
    String except(Throwable t){
        return t.getMessage();
    }

    String block(BlockException e) {
        return "block";
    }

    @RequestMapping("/testParams")
    @SentinelResource("testParams")   //注意这里需要添加@SentinelResource才可以，用户资源名称就使用这里定义的资源名称
    String findUserBorrows2(@RequestParam(value = "a", required = false) String a,
                            @RequestParam(value = "b", required = false) String b,
                            @RequestParam(value = "c",required = false) String c) {
        return "请求成功！a = "+a+", b = "+b+", c = "+c;
    }
}
