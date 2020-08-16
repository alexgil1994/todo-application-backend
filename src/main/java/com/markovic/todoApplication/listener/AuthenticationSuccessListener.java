package com.markovic.todoApplication.listener;

import com.markovic.todoApplication.domain.User;
import com.markovic.todoApplication.repositories.UserRepository;
import com.markovic.todoApplication.services.LoginAttemptService;
import com.markovic.todoApplication.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class AuthenticationSuccessListener {

    @Autowired
    private LoginAttemptService loginAttemptService;

    // Mine
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private UserServiceImpl userServiceImpl;

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) throws ExecutionException {
        Object principal = event.getAuthentication().getPrincipal();
        // Checking if what we are getting from the authentication is a String because we use username
        if (principal instanceof User) {
            User user = (User) event.getAuthentication().getPrincipal();
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
            // Mine
//            userServiceImpl.loginAttemptSuccededClearing(username);
        }
    }
}
