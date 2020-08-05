package com.markovic.todoApplication.controllers;

import com.markovic.todoApplication.domain.Todo;
import com.markovic.todoApplication.services.TodoServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(TodoController.BASE_URL)
public class TodoController {

    public static final String BASE_URL = "v1/todo";

    private TodoServiceImpl todoService;

    @CrossOrigin
    @GetMapping("/getTodosByUsername")
    @ResponseStatus(HttpStatus.OK)
    public Set<Todo> getTodosByUserUsername(String username){
        return todoService.getTodoListByUsername(username);
    }

}
