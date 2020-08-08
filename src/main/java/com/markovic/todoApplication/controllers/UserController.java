package com.markovic.todoApplication.controllers;

import com.markovic.todoApplication.domain.User;
import com.markovic.todoApplication.services.UserServiceImpl;
import com.markovic.todoApplication.v1.model.UserDTO;
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
    public User register(@RequestBody UserDTO userDTO){
        return userServiceImpl.register(userDTO.getFirst_name(), userDTO.getLast_name(), userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail(), userDTO.getIp());
    }

    // TODO: 8/7/2020 Mine
    @CrossOrigin
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public User login(@RequestBody UserDTO userDTO){
        return userServiceImpl.login(userDTO.getFirst_name(), userDTO.getLast_name(), userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail(), userDTO.getIp());
    }

    // TODO: 8/8/2020 Mine
    @CrossOrigin
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestParam String username){
        userServiceImpl.deleteUser(username);
    }

}
