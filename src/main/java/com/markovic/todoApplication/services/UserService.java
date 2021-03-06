package com.markovic.todoApplication.services;

import com.markovic.todoApplication.domain.User;
import com.markovic.todoApplication.v1.model.ResetPasswordUserDTO;
import com.markovic.todoApplication.v1.model.UpdatePasswordUserDTO;
import com.markovic.todoApplication.v1.model.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;

@Service
public interface UserService {

    User register(String first_name, String last_name, String username, String password, String email, String ip) throws MessagingException;

    ResponseEntity<User> login(String first_name, String last_name, String username, String password);

    List<User> getUsers();

    User checkUserByUsername(String username);

    User findUserByUsername(String tokenUsername);

    User findUserById(Long id);

    User checkUserByEmail(String email);

    User findUserByEmail(String email);

    Page<User> getAllByPaging(Integer page);


    void deleteUser(String tokenUsername);

    User patchUser(UserDTO userDTO, String tokenUsername);

    User patchUsernameOfUser(UserDTO userDTO, String tokenUsername);

    User updatePassword(UpdatePasswordUserDTO updatePasswordUserDTO, String tokenUsername) throws MessagingException;

    void resetPassword(ResetPasswordUserDTO resetPasswordUserDTO);

    void promoteUserToAdmin(String tokenUsername);

    // An admin registers a new User as Admin Role
    User registerNewUserByAdmin(String first_name, String last_name, String username, String email);

    User registerNewAdminByAdmin(String first_name, String last_name, String username, String email);

    List<User> searchUsersContaining(String searchInput);
}
