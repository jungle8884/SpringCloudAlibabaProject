package com.test.service.client;

import com.test.entity.User;
import com.test.service.client.impl.UserClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Jungle
 * @version 2023/09/26 12:21
**/
@FeignClient(value = "user-service", fallback = UserClientFallback.class)
public interface UserClient {

    @RequestMapping("/user/{uid}")
    User getUserById(@PathVariable("uid") int uid);
}
