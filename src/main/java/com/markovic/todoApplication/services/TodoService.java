package com.markovic.todoApplication.services;

import com.markovic.todoApplication.domain.Todo;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface TodoService {

    Todo getById(Long id);

    Todo getByUuid(String uuid);

    Set<Todo> getTodoListByUsername(String username);
}
