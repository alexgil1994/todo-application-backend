package com.markovic.todoApplication.controllers;

import com.markovic.todoApplication.domain.User;
import com.markovic.todoApplication.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UserController.BASE_URL)
public class UserController {

    public static final String BASE_URL = "v1/user";

    @Autowired
    private UserServiceImpl userServiceImpl;

    // TODO: 7/27/2020
    @CrossOrigin
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public User register(@RequestBody User user){
        return userServiceImpl.register(user.getFirst_name(), user.getLast_name(), user.getUsername(), user.getPassword(), user.getEmail());
    }
}
