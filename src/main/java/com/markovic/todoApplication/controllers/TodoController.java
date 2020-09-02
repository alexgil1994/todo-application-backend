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

    // TODO: 8/26/2020 Change so that the addnew and patch will be through here instead of user controller


    // TODO: 8/8/2020 Mine
    @CrossOrigin
    @GetMapping("/getAllTodosByUsername")
    @ResponseStatus(HttpStatus.OK)
    public Set<Todo> getTodosByUsername(@RequestParam String username){
        return todoService.getTodoListByUsername(username);
    }

    // TODO: 8/5/2020 Similar but with paging
    @CrossOrigin
    @DeleteMapping("/deleteTodoByIdAndUsername")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTodoById(@RequestParam Long id, @RequestParam String username){
        todoService.deleteTodoById(id, username);
    }

}
