package com.markovic.todoApplication.listener;

import com.markovic.todoApplication.domain.User;
import com.markovic.todoApplication.repositories.UserRepository;
import com.markovic.todoApplication.services.UserServiceImpl;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ExecutionException;

// Used for to implement methods when the user fails to login using eventListener for Spring Security auth
@Component
public class AuthenticationFailureListener {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @EventListener
    public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) throws ExecutionException {
        Object principal = event.getAuthentication().getPrincipal();
        // Checking if what we are getting from the authentication is a String because we use username
        if (principal instanceof String) {
            String username = (String) event.getAuthentication().getPrincipal();
            // Mine
            userServiceImpl.secureUserFromBruteForceAttack(username);
        }
    }
}
