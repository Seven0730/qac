package com.team12.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/user")
public class UserController{

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void registerUser(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        log.info("Registering user: {}", userRegistrationRequest);
        userService.registerUser(userRegistrationRequest);
    }

    @GetMapping
    public void getUser(@RequestParam String username) {
        log.info("Getting user: {}", username);
    }
}
