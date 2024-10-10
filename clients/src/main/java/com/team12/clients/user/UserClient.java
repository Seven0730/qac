package com.team12.clients.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user", path = "api/v1/user")
public interface UserClient {

    @GetMapping
    void getUser(@RequestParam String username);

}
