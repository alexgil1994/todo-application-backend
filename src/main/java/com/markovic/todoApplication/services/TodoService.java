package com.markovic.todoApplication.services;

import com.markovic.todoApplication.domain.Todo;
import com.markovic.todoApplication.v1.model.TodoDTO;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface TodoService {

    Todo findTodoById(Long id);

    Todo findTodoByUuid(String uuid);

    Set<Todo> getTodoListByUsername(String tokenUsername);

    Todo addNewTodo(TodoDTO todoDTO, String tokenUsername);

    Todo patchTodo(TodoDTO todoDTO, String tokenUsername);

    void deleteTodoById(Long id, String tokenUsername);
}
