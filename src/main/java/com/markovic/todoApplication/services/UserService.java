package com.markovic.todoApplication.services;

import com.markovic.todoApplication.domain.Todo;
import com.markovic.todoApplication.domain.User;
import com.markovic.todoApplication.v1.model.ResetPasswordUserDTO;
import com.markovic.todoApplication.v1.model.UpdatePasswordUserDTO;
import com.markovic.todoApplication.v1.model.TodoDTO;
import com.markovic.todoApplication.v1.model.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    User getById(Long id);

    User register(String first_name, String last_name, String username, String password, String email, String ip);

    ResponseEntity<User> login(String first_name, String last_name, String username, String password);

    List<User> getUsers();

    User checkUserByUsername(String username);

    User findUserByUsername(String username);

    User checkUserByEmail(String email);

    User findUserByEmail(String email);

    Page<User> getAllByPaging(Integer page);


    // Not really needed
    User addUser(UserDTO userDTO);

    void deleteUser(String username);

    User patchUser(UserDTO userDTO);

    Todo addNewTodo(TodoDTO todoDTO);

    Todo patchTodo(TodoDTO todoDTO);

    User patchUsernameOfUser(UserDTO userDTO);

    User updatePassword(UpdatePasswordUserDTO updatePasswordUserDTO);

    void resetPassword(ResetPasswordUserDTO resetPasswordUserDTO);
}
