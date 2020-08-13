package com.markovic.todoApplication.controllers;

import com.markovic.todoApplication.domain.Todo;
import com.markovic.todoApplication.domain.User;
import com.markovic.todoApplication.services.UserServiceImpl;
import com.markovic.todoApplication.v1.model.TodoDTO;
import com.markovic.todoApplication.v1.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UserController.BASE_URL)
public class UserController {

    public static final String BASE_URL = "v1/user";

    @Autowired
    private UserServiceImpl userServiceImpl;

    @CrossOrigin
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public User register(@RequestBody UserDTO userDTO){
        return userServiceImpl.register(userDTO.getFirst_name(), userDTO.getLast_name(), userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail(), userDTO.getIp());
    }

    @CrossOrigin
    @PatchMapping("/patchUser")
    @ResponseStatus(HttpStatus.OK)
    public User patchUser(@RequestBody UserDTO userDTO){
        return userServiceImpl.patchUser(userDTO);
    }

    @CrossOrigin
    @PatchMapping("/patchUsernameOfUser")
    @ResponseStatus(HttpStatus.OK)
    public User patchUsernameOfUser(@RequestBody UserDTO userDTO){
        return userServiceImpl.patchUsernameOfUser(userDTO);
    }

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody UserDTO userDTO){
        return userServiceImpl.login(userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail(), userDTO.getIp());
    }

    @CrossOrigin
    @PostMapping("/addNewTodo")
    @ResponseStatus(HttpStatus.OK)
    public Todo addNewTodo(@RequestBody TodoDTO todoDTO){
        return userServiceImpl.addNewTodo(todoDTO);
    }

    @CrossOrigin
    @PatchMapping("/patchTodo")
    @ResponseStatus(HttpStatus.OK)
    public Todo patchTodo(@RequestBody TodoDTO todoDTO) { return userServiceImpl.patchTodo(todoDTO); }

    // TODO: 8/8/2020 Mine
    @CrossOrigin
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestParam String username){
        userServiceImpl.deleteUser(username);
    }

}
