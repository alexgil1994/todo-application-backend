package com.markovic.todoApplication.services;

import com.markovic.todoApplication.domain.Todo;
import com.markovic.todoApplication.domain.User;
import com.markovic.todoApplication.repositories.TodoRepository;
import com.markovic.todoApplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Todo getById(Long id) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isPresent()) return optionalTodo.get();
        else throw new RuntimeException("There was no Todo found with id of: " + id);
    }

    @Override
    public Todo getByUuid(String uuid) {
        Optional<Todo> optionalTodo = todoRepository.findByUuid(uuid);
        if (optionalTodo.isPresent()) return optionalTodo.get();
        else throw new RuntimeException("There was no Todo found with uuid of: " + uuid);
    }

    @Override
    public Set<Todo> getTodoListByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            // Using byUserIs instead of id or username because jpa needs the whole object
            return todoRepository.getTodoListByUserIs(user);
        } else {
            throw new RuntimeException("User with username of: " + username + " does not exist");
        }
    }

}
