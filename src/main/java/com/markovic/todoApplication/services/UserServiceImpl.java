package com.markovic.todoApplication.services;

import com.markovic.todoApplication.domain.Stigma;
import com.markovic.todoApplication.domain.User;
import com.markovic.todoApplication.repositories.StigmaRepository;
import com.markovic.todoApplication.repositories.UserRepository;
import com.markovic.todoApplication.utility.JWTTokenProvider;
import com.markovic.todoApplication.v1.model.UserDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.markovic.todoApplication.constant.SecurityConstant.JWT_TOKEN_HEADER;
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
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTTokenProvider jwtTokenProvider;


    // TODO: 7/27/2020 He again uses a Constructor and autowiring him instead of this way
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User getById(Long id) {
        try {
            Optional<User> optionalUser = userRepository.findById(id);
            return optionalUser.orElse(null);
        }catch (Exception e){
            throw e;
        }
    }


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
        return userRepository.save(newUser);
    }

    // TODO: 8/8/2020 FIXXX StackOverFlowError: null when trying to login while permitting all links
    @Override
    public ResponseEntity<User> login(String username, String password, String email, String ip) {
        authenticate(username, password);
        User existingUser = findUserByUsername(username);
        // TODO: 8/10/2020 Implement to also send an email if the ip doesn't already exist as a Stigma
        if (checkStigma(ip)) existingUser.addStigma(new Stigma(ip));
        HttpHeaders jwtHeader = getJwtHeader(existingUser);
        return new ResponseEntity<>(existingUser, jwtHeader, HttpStatus.OK);
    }

    private HttpHeaders getJwtHeader(User existingUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(existingUser));
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
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
        return null;
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



    // For authentication
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            // Setting the displaying to be the previously last login date and changing the last login date to be now
            user.setLast_login_date_display(user.getLast_login_date());
            user.setLast_login_date(new Date());
            // Saving the user and returning a UserDetails user
            return userRepository.save(user);
        } else {
            throw new UsernameNotFoundException("User not found by username: " + username);
        }
    }


    @Override
    public Page<User> getAllByPaging(Integer page) {
        return null;
    }


    // TODO: 7/14/2020 Implement the rest of the methods
    @Override
    public User addUser(UserDTO userDTO) {
        return null;
    }

    // TODO: 8/7/2020 Mine
    @Override
    public void deleteUser(String username) {
        // Checks - Exceptions in the method
        User user = findUserByUsername(username);
        // Checking Id's, if true delete it else throw exception
        userRepository.deleteById(user.getId());
        // Check if stills exists after deleting
        if (checkUserById(user.getId())){
            throw new RuntimeException("The User with Username of: " + username + "wasn't deleted since it was found again");
        }
    }

    // Doesn't throw exceptions, its a helper method. If exists, return true, if not then false
    private boolean checkUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.isPresent();
    }

    @Override
    public boolean patchUser(Long id, UserDTO userDTO) {
        return false;
    }

}
