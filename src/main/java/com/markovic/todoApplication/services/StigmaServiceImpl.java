package com.markovic.todoApplication.services;

import com.markovic.todoApplication.domain.Stigma;
import com.markovic.todoApplication.domain.User;
import com.markovic.todoApplication.repositories.StigmaRepository;
import com.markovic.todoApplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class StigmaServiceImpl implements StigmaService {

    @Autowired
    private StigmaRepository stigmaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @Override
    public Set<Stigma> getStigmaListByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            // Using byUserIs instead of id or username because jpa needs the whole object
            return stigmaRepository.getStigmaListByUserIs(user);
        } else {
            throw new RuntimeException("User with username of: " + username + " does not exist");
        }
    }

    @Override
    public void deleteStigma(Long id, String username) {
        // Checks - Exceptions in the method
        User user = userService.findUserByUsername(username);
        // Checks - Exceptions in the method
        Stigma stigma = findStigmaById(id);
        // Checking Id's, if true delete it else throw exception
        if (checkIfStigmaIdAndUsernameFit(stigma, user)){
            stigmaRepository.deleteById(id);
            // Check if stills exists after deleting
            if (checkStigmaById(id)){
                throw new RuntimeException("The Stigma with Id of: " + id + "wasn't deleted since it was found again");
            }
        } else throw new RuntimeException("The User's id and the connected User's id from the Stigma didn't match");
    }

    // Throws Exceptions if not found
    private Stigma findStigmaById(Long id) {
        Optional<Stigma> optionalStigma = stigmaRepository.findById(id);
        if (optionalStigma.isPresent()){
            return optionalStigma.get();
        } else throw new RuntimeException("Stigma with id of: " + id + " wasn't found.");
    }

    // Doesn't throw exceptions, its a helper method. If exists, return true, if not then false
    private boolean checkStigmaById(Long id) {
        Optional<Stigma> optionalStigma = stigmaRepository.findById(id);
        return optionalStigma.isPresent();
    }

    // Checks if user's ids match between
    private boolean checkIfStigmaIdAndUsernameFit(Stigma stigma, User user){
        return stigma.getUser().getId().equals(user.getId());
    }
}
