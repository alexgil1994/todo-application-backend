package com.markovic.todoApplication.services;

import com.markovic.todoApplication.domain.Todo;
import com.markovic.todoApplication.domain.User;
import com.markovic.todoApplication.repositories.TodoRepository;
import com.markovic.todoApplication.repositories.UserRepository;
import com.markovic.todoApplication.v1.model.TodoDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Set<Todo> getTodoListByUsername(String tokenUsername) {
        Optional<User> optionalUser = userRepository.findByUsername(tokenUsername);
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            // Using byUserIs instead of id or username because jpa needs the whole object
            return todoRepository.getTodoListByUserIs(user);
        } else {
            throw new RuntimeException("User with username of: " + tokenUsername + " does not exist");
        }
    }

    @Override
    public Todo addNewTodo(TodoDTO todoDTO, String tokenUsername) {
        User existingUser = userService.findUserByUsername(tokenUsername);
        Todo newTodo = createNewTodo(todoDTO);
        // This automatically triggers the save to the db
        existingUser.addTodo(newTodo);
        userRepository.save(existingUser);
        Optional<Todo> optionalTodo = todoRepository.findByUuid(newTodo.getUuid());
        if (optionalTodo.isPresent()) return optionalTodo.get();
        else throw new RuntimeException("There was a problem trying to save the new Todo with uuid of: " + newTodo.getUuid() + ".");
    }

    private Todo createNewTodo(TodoDTO todoDTO) {
        Todo newTodo = new Todo();
        newTodo.setTitle(todoDTO.getTitle());
        newTodo.setDescription(todoDTO.getDescription());
        newTodo.setDate_deadline(todoDTO.getDate_deadline());
        newTodo.setFinished(todoDTO.isFinished());
        newTodo.setUuid(RandomStringUtils.randomAlphanumeric(14));
        return newTodo;
    }

    @Override
    public Todo patchTodo(TodoDTO todoDTO, String tokenUsername) {
        User existingUser = userService.findUserByUsername(tokenUsername);
        Todo existingTodo = findTodoById(todoDTO.getId());
        if (existingTodo.getUser().getId().equals(existingUser.getId())){
            if (todoDTO.getTitle() != null) existingTodo.setTitle(todoDTO.getTitle());
            if (todoDTO.getDescription() != null) existingTodo.setDescription(todoDTO.getDescription());
            if (todoDTO.getDate_deadline() != null) existingTodo.setDate_deadline(todoDTO.getDate_deadline());
            return todoRepository.save(existingTodo);
        } else throw new RuntimeException("Todo with id of: " + todoDTO.getId() + " is not connected with User with id of: " + existingUser.getId());
    }

    @Override
    public void deleteTodoById(Long id, String tokenUsername) {
        // Checks - Exceptions in the method
        User user = userService.findUserByUsername(tokenUsername);
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
    public Todo findTodoById(Long id) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isPresent()){
            return optionalTodo.get();
        } else throw new RuntimeException("Todo with id of: " + id + " wasn't found.");
    }

    // Throws Exceptions if not found
    @Override
    public Todo findTodoByUuid(String uuid) {
        Optional<Todo> optionalTodo = todoRepository.findByUuid(uuid);
        if (optionalTodo.isPresent()) return optionalTodo.get();
        else throw new RuntimeException("There was no Todo found with uuid of: " + uuid);
    }

    // Doesn't throw exceptions, its a helper method. If exists, return true, if not then false
    public boolean checkTodoById(Long id) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        return optionalTodo.isPresent();
    }

    // Checks if user's ids match between
    public boolean checkIfTodoIdAndUsernameFit(Todo todo, User user){
        return todo.getUser().getId().equals(user.getId());
    }

}
