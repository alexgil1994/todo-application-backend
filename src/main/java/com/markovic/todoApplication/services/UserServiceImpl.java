package com.markovic.todoApplication.services;

import com.markovic.todoApplication.domain.User;
import com.markovic.todoApplication.repositories.UserRepository;
import com.markovic.todoApplication.v1.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User getById(Long id) {
        try {
            Optional<User> optionalProfile = userRepository.findById(id);
            return optionalProfile.orElse(null);
        }catch (Exception e){
            throw e;
        }
    }

    // TODO: 7/14/2020 Implement the rest of the methods
    @Override
    public User addProfile(UserDTO userDTO) {
        return null;
    }

    @Override
    public boolean deleteProfile(Long id) {
        return false;
    }

    @Override
    public boolean patchProfile(Long id, UserDTO userDTO) {
        return false;
    }

    @Override
    public Page<User> getAllByPaging(Integer page) {
        return null;
    }

}
