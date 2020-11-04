package com.markovic.todoApplication.services;

import com.markovic.todoApplication.domain.Stigma;
import com.markovic.todoApplication.domain.User;
import com.markovic.todoApplication.repositories.StigmaRepository;
import com.markovic.todoApplication.repositories.TodoRepository;
import com.markovic.todoApplication.repositories.UserRepository;
import com.markovic.todoApplication.utility.JWTTokenProvider;
import com.markovic.todoApplication.v1.model.ResetPasswordUserDTO;
import com.markovic.todoApplication.v1.model.UpdatePasswordUserDTO;
import com.markovic.todoApplication.v1.model.UserDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.markovic.todoApplication.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static com.markovic.todoApplication.enumeration.Role.ROLE_ADMIN;
import static com.markovic.todoApplication.enumeration.Role.ROLE_USER;

// We are using the qualifier to set another name for this service in order to use it in auth in the SecurityConfiguration
//@Qualifier("userDetailService")
@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StigmaRepository stigmaRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoServiceImpl todoServiceImpl;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    private EmailService emailService;


    // TODO: 7/27/2020 He again uses a Constructor and autowiring him instead of this way
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;



    // TODO: 9/3/2020 ---Implement in the register to send an email to the user creating a link as http://localhost:8080/user/verifyEmail/{token} sending the token through in the link and having an endpoint where I will verify the token to verify the email activating this way in the end the new user.

    // TODO: 8/7/2020 Implement checking if the user already exists as ipUser and transfer
    // Registration method
    @Override
    public User register(String first_name, String last_name, String username, String password, String email, String ip) {
        // Validating that a user with this username or email doesn't already exist
        validateNewUsernameAndEmail(username, email);
        User newUser = new User();
        newUser.setUuid(RandomStringUtils.randomAlphanumeric(14));
        newUser.setFirst_name(first_name);
        newUser.setLast_name(last_name);
        newUser.setUsername(username);
        newUser.setEmail(email);
        // Encoding the password
        String encoded_password = encodePassword(password);
        newUser.setPassword(encoded_password);
        newUser.setAdded_date(new Date());
        newUser.setIs_not_locked(true);
        newUser.setIs_enabled(true);
        // Setting Role
        newUser.setUser_role(ROLE_USER.name());
        // Setting Authorities
        newUser.setUser_authorities(ROLE_USER.getAuthorities());
        // TODO: Set-up with Cloudinary to use for images, having a default in there and saving others
        newUser.setImage_url(null);
        // Checking if ip is blank, if not then creating a new Stigma, setting ip and connecting it with the user
        if (checkStigma(ip)) newUser.addStigma(new Stigma(ip));
        newUser = userRepository.save(newUser);
        try {
            emailService.sendNewUserEmail(newUser.getUsername(), newUser.getEmail());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return newUser;
    }

    @Override
    public ResponseEntity<User> login(String username, String password, String email, String ip) {
        User existingUser;
        if (StringUtils.isNotEmpty(username) && StringUtils.isNotBlank(username)) { // Checking if the User didn't remember username
            authenticate(username, password);
            existingUser = findUserByUsername(username);
            checkUserActivity(existingUser, ip); // Checks if it's a new device or ip and if so, adds it and sends email
            HttpHeaders jwtHeader = getJwtHeader(existingUser);
            return new ResponseEntity<>(existingUser, jwtHeader, HttpStatus.OK);
        } else if (StringUtils.isNotEmpty(email) && StringUtils.isNotBlank(email)){ // If User didn't give username for login but did give email and password
            existingUser = findUserByEmail(email);
            authenticate(existingUser.getUsername(), password);
            checkUserActivity(existingUser, ip); // Checks if it's a new device or ip and if so, adds it and sends email
            HttpHeaders jwtHeader = getJwtHeader(existingUser);
            return new ResponseEntity<>(existingUser, jwtHeader, HttpStatus.OK);
        } else throw new RuntimeException("The User didn't give neither username nor email for the authentication");
    }

    // All the checks happen through it using automatically the loadUserByUsername()
    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    // ** For authentication used by Spring Security to load the user and manipulating it to secure from Brute Force Attack
    @Override
    public UserDetails loadUserByUsername(String username) {
        User existingUser = findUserByUsername(username);
        validateAttempt(existingUser);
        // Setting the displaying to be the previously last login date and changing the last login date to be now
        existingUser.setLast_login_date_display(existingUser.getLast_login_date());
        existingUser.setLast_login_date(new Date());
        // Saving the user and returning a UserDetails user
        return userRepository.save(existingUser);

    }

    // Securing from Brute Force Attack
    private void validateAttempt(User existingUser)  {
        if (existingUser.getIs_not_locked()){
            if (loginAttemptService.hasExceededMaxAttempts(existingUser.getUsername())) {
                existingUser.setIs_not_locked(false);
            } else existingUser.setIs_not_locked(true);
        } else loginAttemptService.evictUserFromLoginAttemptCache(existingUser.getUsername());
    }

    private HttpHeaders getJwtHeader(User existingUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(existingUser));
        return headers;
    }

    // Checking if user's stigma already exists, otherwise we inform with an email
    private void checkUserActivity(User existingUser, String ip) {
        if (checkStigma(ip) && !checkIfStigmaExistsInUserSet(ip, existingUser)) {
            existingUser.addStigma(new Stigma(ip));
            userRepository.save(existingUser);
            try {
                emailService.sendUserNewActivity(existingUser.getUsername(), existingUser.getEmail(), ip);
            } catch (Exception e) {
//              Not throwing the exception in case I couldn't send an email so that I don't stop the login completion
//              e.printStackTrace();
            }
         }
    }
    
    // TODO: 8/16/2020 Impl for more than just ip
    private boolean checkIfStigmaExistsInUserSet(String ip, User existingUser) {
        return stigmaRepository.getStigmaByIpAndUserIs(ip, existingUser) != null;
    }

    // Checking if ip is null
    private boolean checkStigma(String ip) {
        return StringUtils.isNotBlank(ip);
    }

    // Encoding the password with bcrypt
    private String encodePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    private void validateNewUsernameAndEmail(String username, String email) {
        // We are using the check methods instead of load because they can return null, while load methods would throw an Exception
        User userByUsername = checkUserByUsername(username);
        User userByEmail = checkUserByEmail(email);
        if (userByUsername != null || userByEmail != null){
            throw new RuntimeException("User already exists");
        }
    }


    @Override
    public List<User> getUsers() {
        List<User> usersList = new ArrayList<>();
        userRepository.findAll().forEach(usersList::add);
        return usersList;
    }

    @Override
    public User checkUserByUsername(String username) {
        // Checking to see if it is null, empty or having only spaces
        if (StringUtils.isNotBlank(username)){
            Optional<User> optionalUser = userRepository.findByUsername(username);
            return optionalUser.orElse(null);
        } else {
            throw new IllegalArgumentException("The specified argument username of: " + username + " is not wrongly inputted");
        }
    }

    @Override
    public User findUserByUsername(String username) {
        // Checking to see if it is null, empty or having only spaces
        if (StringUtils.isNotBlank(username)){
            Optional<User> optionalUser = userRepository.findByUsername(username);
            if (optionalUser.isPresent()){
                return optionalUser.get();
            } else throw new RuntimeException("User with username of: " + username + " wasn't found.");
        } else {
            throw new IllegalArgumentException("The specified argument username of: " + username + " is not wrongly inputted");
        }
    }

    @Override
    public User findUserById(Long id) {
        try {
            Optional<User> optionalUser = userRepository.findById(id);
            return optionalUser.orElse(null);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public User checkUserByEmail(String email) {
        // Checking to see if it is null, empty or having only spaces
        if (StringUtils.isNotBlank(email)){
            Optional<User> optionalUser = userRepository.findByEmail(email);
            return optionalUser.orElse(null);
        } else {
            throw new IllegalArgumentException("The specified argument email of: " + email + " is not wrongly inputted");
        }
    }

    @Override
    public User findUserByEmail(String email) {
        // Checking to see if it is null, empty or having only spaces
        if (StringUtils.isNotBlank(email)){
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()){
                return optionalUser.get();
            } else throw new RuntimeException("User with email of: " + email + " wasn't found.");
        } else {
            throw new IllegalArgumentException("The specified argument email of: " + email + " is not wrongly inputted");
        }
    }

    @Override
    public Page<User> getAllByPaging(Integer page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.Direction.DESC);
        return userRepository.findAll(pageable);
    }

    @Override
    public void deleteUser(String tokenUsername) {
        // Checks - Exceptions in the method
        User user = findUserByUsername(tokenUsername);
        // Checking Id's, if true delete it else throw exception
        userRepository.deleteById(user.getId());
        // Check if stills exists after deleting
        if (checkUserById(user.getId())){
            throw new RuntimeException("The User with Username of: " + tokenUsername + "wasn't deleted since it was found again");
        }
    }

    // Doesn't throw exceptions, its a helper method. If exists, return true, if not then false
    private boolean checkUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.isPresent();
    }

    // TODO: 8/11/2020 --How to make it SECURE knowing surely that the user that triggers the request is actually the one that asks the request and not someone else that by-passed (got it somehow) JWT token. --Could be with actually checking the token de-encrypting it to see the actual username (Learn how to do this also manually (Spring security does it as well))
    //  ---Could be solved by using preAuthorize annotation in the controller that the username is == with the authentication context username.
    @Override
    public User patchUser(UserDTO userDTO, String tokenUsername) {
        // Throwing exceptions in the method
        User existingUser = findUserByUsername(tokenUsername);
//        if (checkIfEditIsBySameUser(tokenUsername, existingUser)){ // Not needed anymore since I am getting the user from the token itself
            if (userDTO.getFirst_name() != null) existingUser.setFirst_name(userDTO.getFirst_name());
            if (userDTO.getLast_name() != null) existingUser.setLast_name(userDTO.getLast_name());
            if (userDTO.getEmail() != null) existingUser.setEmail(userDTO.getEmail());
            // TODO: 8/11/2020 Handle it differently sending an email also to require access for such change
            if (userDTO.getEmail() != null) {
                // Patching Email Checking method will return null if not found (Exception-safe-method)
                if (checkUserByEmail(userDTO.getEmail()) != null){
                    existingUser.setEmail(userDTO.getEmail());
                }
            }
//        }
        return userRepository.save(existingUser);
    }

    // Checking if usernames match
    private boolean checkIfEditIsBySameUser(String username, User existingUser) {
        return username.equals(existingUser.getUsername());
    }

    // TODO: 8/11/2020 Find a better approach
    @Override
    public User patchUsernameOfUser(UserDTO userDTO, String tokenUsername) {
        // Does all the checks-exceptions inside
        User existingUser = findUserByEmail(userDTO.getEmail());
        if (StringUtils.isNotBlank(tokenUsername) && StringUtils.isNotEmpty(tokenUsername) && checkUserByUsername(tokenUsername) != null){ // Not really needed to check these things anymore
            existingUser.setEmail(userDTO.getEmail());
            return existingUser;
        } else throw new RuntimeException("Username param was either empty, blank or User didn't exist in the db with such.");
    }

    // TODO: 8/16/2020 --Test
    @Override
    public User updatePassword(UpdatePasswordUserDTO updatePasswordUserDTO, String tokenUsername) {
        User existingUser = findUserByUsername(tokenUsername);
        if (bCryptPasswordEncoder.encode(updatePasswordUserDTO.getOld_password()).equals(existingUser.getPassword()) // Checking vars
                && StringUtils.isNotEmpty(updatePasswordUserDTO.getNew_password())
                && StringUtils.isNotBlank(updatePasswordUserDTO.getNew_password())) {
            String newPassword = bCryptPasswordEncoder.encode(updatePasswordUserDTO.getNew_password()); // Encoding the new password
            existingUser.setPassword(newPassword);
            existingUser = userRepository.save(existingUser);
            try {
                emailService.sendUpdateUserPasswordOccurred(existingUser.getUsername(), existingUser.getEmail()); // Informing the User that his password has been updated
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return existingUser;
    }

    @Override
    public void resetPassword(ResetPasswordUserDTO resetPasswordUserDTO) {
        User existingUser; // Checking if the User exists based on either the username or the email
        if (StringUtils.isNotEmpty(resetPasswordUserDTO.getUsername()) && StringUtils.isNotBlank(resetPasswordUserDTO.getUsername())) {
            existingUser = findUserByUsername(resetPasswordUserDTO.getUsername());
        } else if (StringUtils.isNotEmpty(resetPasswordUserDTO.getEmail()) && StringUtils.isNotBlank(resetPasswordUserDTO.getEmail())){
            existingUser = findUserByEmail(resetPasswordUserDTO.getEmail());
        } else throw new RuntimeException("There was no User found with the specified username or email");
        String newPassword = RandomStringUtils.randomAlphanumeric(10); // Creating the new password, encrypting it and saving the user to the db
        existingUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
        try {
            User returnedUser = userRepository.save(existingUser);
            emailService.sendResetNewPassword(returnedUser.getUsername(), returnedUser.getEmail(), newPassword); // Sending to the User the new password through his email
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: 9/6/2020 Test
    @Override
    public void promoteUserToAdmin(String tokenUsername) {
        User existingUser = findUserByUsername(tokenUsername); // Does the checks
        try {
            existingUser.setUser_role(ROLE_ADMIN.name());
            existingUser.setUser_authorities(ROLE_ADMIN.getAuthorities());
            userRepository.save(existingUser);
        }catch (Exception e) {
            throw new RuntimeException("There was an Error trying to promote the User " + tokenUsername + " to an Admin role.");
        }
    }

    // An admin registers a new User as Admin Role
    @Override
    public User registerNewUserByAdmin(String first_name, String last_name, String username, String email) {
        // Validating that a user with this username or email doesn't already exist
        validateNewUsernameAndEmail(username, email);
        User newUser = new User();
        newUser.setUuid(RandomStringUtils.randomAlphanumeric(14));
        newUser.setFirst_name(first_name);
        newUser.setLast_name(last_name);
        newUser.setUsername(username);
        newUser.setEmail(email);
        // Encoding the password
        String password = RandomStringUtils.randomAlphanumeric(12);
        String encoded_password = encodePassword(password);
        newUser.setPassword(encoded_password);
        newUser.setAdded_date(new Date());
        newUser.setIs_not_locked(true);
        newUser.setIs_enabled(true);
        // Setting Role
        newUser.setUser_role(ROLE_USER.name());
        // Setting Authorities
        newUser.setUser_authorities(ROLE_USER.getAuthorities());
        newUser.setImage_url(null);
        newUser = userRepository.save(newUser);
        try {
            emailService.sendNewUserByAdminEmail(newUser.getUsername(), newUser.getEmail(), password);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return newUser;
    }

    // An admin registers a new User as Admin Role
    @Override
    public User registerNewAdminByAdmin(String first_name, String last_name, String username, String email) {
        // Validating that a user with this username or email doesn't already exist
        validateNewUsernameAndEmail(username, email);
        User newUser = new User();
        newUser.setUuid(RandomStringUtils.randomAlphanumeric(14));
        newUser.setFirst_name(first_name);
        newUser.setLast_name(last_name);
        newUser.setUsername(username);
        newUser.setEmail(email);
        // Encoding the password
        String password = RandomStringUtils.randomAlphanumeric(12);
        String encoded_password = encodePassword(password);
        newUser.setPassword(encoded_password);
        newUser.setAdded_date(new Date());
        newUser.setIs_not_locked(true);
        newUser.setIs_enabled(true);
        // Setting Role
        newUser.setUser_role(ROLE_ADMIN.name());
        // Setting Authorities
        newUser.setUser_authorities(ROLE_ADMIN.getAuthorities());
        newUser.setImage_url(null);
        newUser = userRepository.save(newUser);
        try {
            emailService.sendNewAdminByAdminEmail(newUser.getUsername(), newUser.getEmail(), password);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return newUser;
    }

    @Override
    public List<User> searchUsersContaining(String searchInput) {
        if (StringUtils.isNotEmpty(searchInput) && StringUtils.isNotBlank(searchInput)) {
            return userRepository.findByUsernameContainsIgnoreCase(searchInput);
        } else throw new RuntimeException("SearchInput given by the user was either null or empty.");
    }

}
