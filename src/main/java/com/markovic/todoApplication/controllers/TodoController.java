package com.markovic.todoApplication.controllers;

import com.markovic.todoApplication.domain.Todo;
import com.markovic.todoApplication.services.TodoServiceImpl;
import com.markovic.todoApplication.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(TodoController.BASE_URL)
public class TodoController {

    public static final String BASE_URL = "v1/todo";

    @Autowired
    private TodoServiceImpl todoService;


    @CrossOrigin
    @GetMapping("/getAllTodosByUsername")
    @ResponseStatus(HttpStatus.OK)
    public Set<Todo> getTodosByUserUsername(@RequestBody String username){
        return todoService.getTodoListByUsername(username);
    }

    // TODO: 8/5/2020 Similar but with paging
    @CrossOrigin
    @DeleteMapping("/deleteTodoByIdAndUsername")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTodoById(@RequestBody Long id, String username){
        todoService.deleteTodoById(id, username);
    }

}
