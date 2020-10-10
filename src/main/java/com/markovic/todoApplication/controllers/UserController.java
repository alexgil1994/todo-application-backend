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


    // TODO: 10/10/2020 PreAuthorize annotations for username == principal.username etc. are commented because turns out the spring framework has itself security mechanism to stop such bad actions.
    // TODO: 10/10/2020 Keeping the PreAuthorize annotations for Role == admin because these are business logic authorization checks

    @CrossOrigin
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public User register(@RequestBody UserDTO userDTO) throws MessagingException {
        return userServiceImpl.register(userDTO.getFirst_name(), userDTO.getLast_name(), userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail(), userDTO.getIp());
    }

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody UserDTO userDTO){
        return userServiceImpl.login(userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail(), userDTO.getIp());
    }

    @CrossOrigin
    @GetMapping("/findUserByUsername")
    @ResponseStatus(HttpStatus.OK)
    public User findUser(@RequestParam String username){ return userServiceImpl.findUserByUsername(username); }

    @CrossOrigin
    @GetMapping("/findUserById")
    @ResponseStatus(HttpStatus.OK)
    public User findUser(@RequestParam Long id){ return userServiceImpl.findUserById(id); }

//    // 9/1/2020 Testing only
//    @CrossOrigin
//    @PostMapping("/email")
//    @ResponseStatus(HttpStatus.OK)
//    public void checkEmail(@RequestBody UserDTO userDTO) {
//        emailService.sendNewUserEmail(userDTO.getUsername(), userDTO.getEmail());
//    }

    @CrossOrigin
    @PatchMapping("/patchUser")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("#userDTO.username == principal.username")
    public User patchUser(@RequestBody UserDTO userDTO){
        return userServiceImpl.patchUser(userDTO);
    }

    @CrossOrigin
    @PatchMapping("/patchUsernameOfUser")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("#userDTO.username == principal.username")
    public User patchUsernameOfUser(@RequestBody UserDTO userDTO){
        return userServiceImpl.patchUsernameOfUser(userDTO);
    }

    @CrossOrigin
    @PatchMapping("/updatePassword")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("#updatePasswordUserDTO.username == principal.username")
    public User updatePassword(@RequestBody UpdatePasswordUserDTO updatePasswordUserDTO) throws MessagingException {
        return userServiceImpl.updatePassword(updatePasswordUserDTO);
    }

    @CrossOrigin
    @PatchMapping("/resetPassword")
    @ResponseStatus(HttpStatus.OK)
    public void resetPassword(@RequestBody ResetPasswordUserDTO resetPasswordUserDTO){
        userServiceImpl.resetPassword(resetPasswordUserDTO);
    }

    // TODO: 9/6/2020 Test
    @CrossOrigin
    @PostMapping("/promoteUserToAdmin")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("#username == principal.username")
    public void promoteUserToAdmin(@RequestParam String username){ userServiceImpl.promoteUserToAdmin(username); }

    // TODO: 9/6/2020 Test
    @CrossOrigin
    @PostMapping("/registerNewUserByAdmin")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User registerNewUserByAdmin(@RequestBody UserDTO userDTO){
        return userServiceImpl.registerNewUserByAdmin(userDTO.getFirst_name(), userDTO.getLast_name(), userDTO.getUsername(), userDTO.getEmail());
    }

    // TODO: 9/6/2020 Test
    @CrossOrigin
    @PostMapping("/registerNewAdminByAdmin")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User registerNewAdminByAdmin(@RequestBody UserDTO userDTO){
        return userServiceImpl.registerNewAdminByAdmin(userDTO.getFirst_name(), userDTO.getLast_name(), userDTO.getUsername(), userDTO.getEmail());
    }

    // TODO: 9/6/2020 DELETE USER BY ADMIN
    @CrossOrigin
    @DeleteMapping("/deleteUserByAdmin")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUserByAdmin(@RequestParam String username){
        userServiceImpl.deleteUser(username);
    }

    // TODO: 8/8/2020 Mine
    @CrossOrigin
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("#username == principal.username")
    public void delete(@RequestParam String username){
        userServiceImpl.deleteUser(username);
    }

    @CrossOrigin
    @GetMapping("/getAllByPaging")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("#username == principal.username")
    public Page<User> getAllByPaging(@RequestParam String username, @RequestParam int page){ return userServiceImpl.getAllByPaging(page); }

    @CrossOrigin
    @GetMapping("/getUsers")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers(){ return userServiceImpl.getUsers(); }

}
