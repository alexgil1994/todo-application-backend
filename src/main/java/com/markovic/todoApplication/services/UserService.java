package com.markovic.todoApplication.services;

import com.markovic.todoApplication.domain.User;
import com.markovic.todoApplication.v1.model.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    User getById(Long id);

    User register(String first_name, String last_name, String username, String password, String email);

    List<User> getUsers();

    User checkUserByUsername(String username);

    User findUserByUsername(String username);

    User checkUserByEmail(String email);

    User findUserByEmail(String email);

    Page<User> getAllByPaging(Integer page);


    // Not really needed
    User addUser(UserDTO userDTO);

    boolean deleteUser(Long id);

    boolean patchUser(Long id, UserDTO userDTO);


}
