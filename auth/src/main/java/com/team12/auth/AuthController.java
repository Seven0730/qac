package com.team12.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @GetMapping("/{userId}")
    public String checkAuth(@PathVariable("userId") Integer userId) {
        log.info("Checking auth: {}", userId);
        return "false";
    }

//    @GetMapping
//    public String getAuth() {
//        return "string";
//    }

}
