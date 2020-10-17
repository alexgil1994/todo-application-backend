package com.markovic.todoApplication.controllers;

import com.markovic.todoApplication.domain.Todo;
import com.markovic.todoApplication.services.TodoServiceImpl;
import com.markovic.todoApplication.services.UserServiceImpl;
import com.markovic.todoApplication.v1.model.TodoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(TodoController.BASE_URL)
public class TodoController {

    public static final String BASE_URL = "v1/todo";

    @Autowired
    private TodoServiceImpl todoService;

    // TODO: 10/12/2020 Could be checking if the todo id is actually of this tokenUsername User through Authentication authentication param for ultra safety
    @GetMapping("/findTodoById")
    @ResponseStatus(HttpStatus.OK)
    public Todo findTodoById(@RequestParam Long id){
        return todoService.findTodoById(id);
    }

    // TODO: 10/12/2020 Could be checking if the todo id is actually of this tokenUsername User through Authentication authentication param for ultra safety
    @GetMapping("/findTodoByUuid")
    @ResponseStatus(HttpStatus.OK)
    public Todo findTodoByUuid(@RequestParam String uuid){
        return todoService.findTodoByUuid(uuid);
    }

    @GetMapping("/getAllTodosByUsername")
    @ResponseStatus(HttpStatus.OK)
    public Set<Todo> getTodosByUsername(Authentication authentication){
        return todoService.getTodoListByUsername(authentication.getName());
    }

    @DeleteMapping("/deleteTodoByIdAndUsername")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTodoById(Authentication authentication, @RequestParam Long id){
        todoService.deleteTodoById(id, authentication.getName());
    }

    @PostMapping("/addNewTodo")
    @ResponseStatus(HttpStatus.OK)
    public Todo addNewTodo(Authentication authentication, @RequestBody TodoDTO todoDTO){
        return todoService.addNewTodo(todoDTO, authentication.getName());
    }

    @PatchMapping("/patchTodo")
    @ResponseStatus(HttpStatus.OK)
    public Todo patchTodo(Authentication authentication, @RequestBody TodoDTO todoDTO) { return todoService.patchTodo(todoDTO, authentication.getName()); }

}
