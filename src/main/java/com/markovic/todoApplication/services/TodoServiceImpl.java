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

    @Autowired
    private UserServiceImpl userService;

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

    @Override
    public void deleteTodoById(Long id, String username) {
        // Checks - Exceptions in the method
        User user = userService.findUserByUsername(username);
        // Checks - Exceptions in the method
        Todo todo = findTodoById(id);
        // Checking Id's, if true delete it else throw exception
        if (checkIfTodoIdAndUsernameFit(todo, user)){
            todoRepository.deleteById(id);
            // Check if stills exists after deleting
            if (checkTodoById(id)){
                throw new RuntimeException("The Todo with Id of: " + id + "wasn't deleted since it was found again");
            }
        } else throw new RuntimeException("The User's id and the connected User's id from the Todo didn't match");
    }

    // Throws Exceptions if not found
    private Todo findTodoById(Long id) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isPresent()){
            return optionalTodo.get();
        } else throw new RuntimeException("Todo with id of: " + id + " wasn't found.");
    }

    // Doesn't throw exceptions, its a helper method. If exists, return true, if not then false
    private boolean checkTodoById(Long id) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        return optionalTodo.isPresent();
    }

    // Checks if user's ids match between
    private boolean checkIfTodoIdAndUsernameFit(Todo todo, User user){
        return todo.getUser().getId().equals(user.getId());
    }

}
