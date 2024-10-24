package com.team12.clients.user;

import com.team12.clients.user.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user", path = "api/v1/user")
public interface UserClient {

    @GetMapping
    void getUser(@RequestParam("username") String username);

    @GetMapping("/search")
    List<UserDto> searchUsersByUsername(@RequestParam("keyword") String keyword);
}
