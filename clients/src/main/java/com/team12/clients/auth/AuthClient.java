package com.team12.clients.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth", path = "api/v1/auth")
public interface AuthClient {

//    @GetMapping
//    String getAuth();

    @GetMapping("/{userId}")
    String checkAuth(@PathVariable("userId") Integer userId);

}