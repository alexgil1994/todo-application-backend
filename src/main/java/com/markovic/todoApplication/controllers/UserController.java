package com.markovic.todoApplication.controllers;

import com.markovic.todoApplication.domain.User;
import com.markovic.todoApplication.services.EmailService;
import com.markovic.todoApplication.services.UserServiceImpl;
import com.markovic.todoApplication.v1.model.ResetPasswordUserDTO;
import com.markovic.todoApplication.v1.model.UpdatePasswordUserDTO;
import com.markovic.todoApplication.v1.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequestMapping(UserController.BASE_URL)
public class UserController {

    public static final String BASE_URL = "v1/user";

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private EmailService emailService;


    // TODO: 10/10/2020 Keeping the PreAuthorize annotations for Role == admin because these are business logic authorization checks

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public User register(@RequestBody UserDTO userDTO) throws MessagingException {
        return userServiceImpl.register(userDTO.getFirst_name(), userDTO.getLast_name(), userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail(), userDTO.getIp());
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody UserDTO userDTO){
        return userServiceImpl.login(userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail(), userDTO.getIp());
    }

    @GetMapping("/findUserByUsername")
    @ResponseStatus(HttpStatus.OK)
    public User findUser(Authentication authentication){ return userServiceImpl.findUserByUsername(authentication.getName()); }

    @GetMapping("/findUserById")
    @ResponseStatus(HttpStatus.OK)
    public User findUser(@RequestParam Long id){ return userServiceImpl.findUserById(id); }

    @GetMapping("/visitUserByUsername")
    @ResponseStatus(HttpStatus.OK)
    public User visitUser(@RequestParam String visitUsername){ return userServiceImpl.findUserByUsername(visitUsername); }

    @PatchMapping("/patchUser")
    @ResponseStatus(HttpStatus.OK)
    public User patchUser(Authentication authentication, @RequestBody UserDTO userDTO){
        return userServiceImpl.patchUser(userDTO, authentication.getName());
    }

    @PatchMapping("/patchUsernameOfUser")
    @ResponseStatus(HttpStatus.OK)
    public User patchUsernameOfUser(Authentication authentication, @RequestBody UserDTO userDTO){
        return userServiceImpl.patchUsernameOfUser(userDTO, authentication.getName());
    }

    @PatchMapping("/updatePassword")
    @ResponseStatus(HttpStatus.OK)
    public User updatePassword(Authentication authentication, @RequestBody UpdatePasswordUserDTO updatePasswordUserDTO) throws MessagingException {
        return userServiceImpl.updatePassword(updatePasswordUserDTO, authentication.getName());
    }

    @PatchMapping("/resetPassword")
    @ResponseStatus(HttpStatus.OK)
    public void resetPassword(@RequestBody ResetPasswordUserDTO resetPasswordUserDTO){ // We don't use token for this request, it is public so cant use Authentication to get username here
        userServiceImpl.resetPassword(resetPasswordUserDTO);
    }

    // TODO: 9/6/2020 Test
    @PostMapping("/promoteUserToAdmin")
    @ResponseStatus(HttpStatus.OK)
    public void promoteUserToAdmin(Authentication authentication){ userServiceImpl.promoteUserToAdmin(authentication.getName()); }

    // TODO: 9/6/2020 Test
    @PostMapping("/registerNewUserByAdmin")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')") // It's an admin so we are safe
    public User registerNewUserByAdmin(@RequestBody UserDTO userDTO){
        return userServiceImpl.registerNewUserByAdmin(userDTO.getFirst_name(), userDTO.getLast_name(), userDTO.getUsername(), userDTO.getEmail());
    }

    // TODO: 9/6/2020 Test
    @PostMapping("/registerNewAdminByAdmin")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')") // It's an admin so we are safe
    public User registerNewAdminByAdmin(@RequestBody UserDTO userDTO){
        return userServiceImpl.registerNewAdminByAdmin(userDTO.getFirst_name(), userDTO.getLast_name(), userDTO.getUsername(), userDTO.getEmail());
    }

    // TODO: 9/6/2020 DELETE USER BY ADMIN
    @DeleteMapping("/deleteUserByAdmin")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')") // It's an admin so we are safe
    public void deleteUserByAdmin(@RequestParam String username){
        userServiceImpl.deleteUser(username);
    }

    // TODO: 8/8/2020 Mine
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(Authentication authentication){
        userServiceImpl.deleteUser(authentication.getName());
    }

    @GetMapping("/getAllByPaging")
    @ResponseStatus(HttpStatus.OK) // It needs generally a token
    public Page<User> getAllByPaging(@RequestParam int page) { return userServiceImpl.getAllByPaging(page); }

    @GetMapping("/searchUsers")
    @ResponseStatus(HttpStatus.OK) // It needs generally a token
    public List<User> searchUsers(@RequestParam String searchInput) {
        return userServiceImpl.searchUsersContaining(searchInput);
    }

    @GetMapping("/getUsers")
    @ResponseStatus(HttpStatus.OK) // It needs generally a token
    public List<User> getAllUsers(){ return userServiceImpl.getUsers(); }

}
