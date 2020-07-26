package com.markovic.todoApplication.services;

import com.markovic.todoApplication.domain.User;
import com.markovic.todoApplication.v1.model.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User getById(Long id);

    User addProfile(UserDTO userDTO);

    boolean deleteProfile(Long id);

    boolean patchProfile(Long id, UserDTO userDTO);

    Page<User> getAllByPaging(Integer page);

}
