package com.sesc.studentportal.controller;

import com.sesc.studentportal.model.User;
import com.sesc.studentportal.services.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public User registerUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    public UserController(UserService userService) {
        this.userService = userService;
    }
}
