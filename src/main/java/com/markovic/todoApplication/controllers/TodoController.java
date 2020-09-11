package com.markovic.todoApplication.controllers;

import com.markovic.todoApplication.domain.Todo;
import com.markovic.todoApplication.services.TodoServiceImpl;
import com.markovic.todoApplication.services.UserServiceImpl;
import com.markovic.todoApplication.v1.model.TodoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(TodoController.BASE_URL)
public class TodoController {

    public static final String BASE_URL = "v1/todo";

    @Autowired
    private TodoServiceImpl todoService;


    @CrossOrigin
    @GetMapping("/findTodoById")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("#username == principal.username")
    public Todo findTodoById(@RequestParam String username, @RequestParam Long id){
        return todoService.findTodoById(id);
    }

    @CrossOrigin
    @GetMapping("/findTodoByUuid")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("#username == principal.username")
    public Todo findTodoByUuid(@RequestParam String username, @RequestParam String uuid){
        return todoService.findTodoByUuid(uuid);
    }

    @CrossOrigin
    @GetMapping("/getAllTodosByUsername")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("#username == principal.username")
    public Set<Todo> getTodosByUsername(@RequestParam String username){
        return todoService.getTodoListByUsername(username);
    }

    @CrossOrigin
    @DeleteMapping("/deleteTodoByIdAndUsername")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("#username == principal.username")
    public void deleteTodoById(@RequestParam Long id, @RequestParam String username){
        todoService.deleteTodoById(id, username);
    }

    @CrossOrigin
    @PostMapping("/addNewTodo")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("#todoDTO.username == principal.username")
    public Todo addNewTodo(@RequestBody TodoDTO todoDTO){
        return todoService.addNewTodo(todoDTO);
    }

    @CrossOrigin
    @PatchMapping("/patchTodo")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("#todoDTO.username == principal.username")
    public Todo patchTodo(@RequestBody TodoDTO todoDTO) { return todoService.patchTodo(todoDTO); }

}
