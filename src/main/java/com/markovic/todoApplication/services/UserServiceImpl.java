package com.markovic.todoApplication.services;

import com.markovic.todoApplication.domain.Stigma;
import com.markovic.todoApplication.domain.Todo;
import com.markovic.todoApplication.domain.User;
import com.markovic.todoApplication.repositories.StigmaRepository;
import com.markovic.todoApplication.repositories.TodoRepository;
import com.markovic.todoApplication.repositories.UserRepository;
import com.markovic.todoApplication.utility.JWTTokenProvider;
import com.markovic.todoApplication.v1.model.TodoDTO;
import com.markovic.todoApplication.v1.model.UserDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
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
    private TodoRepository todoRepository;

    @Autowired
    private TodoServiceImpl todoServiceImpl;

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

    @Override
    public ResponseEntity<User> login(String username, String password, String email, String ip) {
        authenticate(username, password);
        User existingUser = findUserByUsername(username);
        // TODO: 8/10/2020 Implement to also send an email if the ip doesn't already exist as a Stigma
        if (checkStigma(ip)) existingUser.addStigma(new Stigma(ip));
        HttpHeaders jwtHeader = getJwtHeader(existingUser);
        return new ResponseEntity<>(existingUser, jwtHeader, HttpStatus.OK);
    }

    // TODO: 8/14/2020 --------- DOESN'T WORK AS EXPECTED. ALSO I NEED TO HAVE IN BOTH SERVICE AND IN THE EVENTS WHICH IS WEIRD - IF I HAVE IT ONLY HERE OR ONLY THERE IT DOESNT WORK. WHEN IT GOES TO ATTEMPTS = 5 THEN IT DOESNT CHANGE ISNOTLOCKED TO FALSE AS I SPECIFY. ALSO I NEED AFTER I FIX , TO ALSO ALSO CHECK THE TIME PERIOD WHEN ISNOTLOCKED IS FALSE.
    private void authenticate(String username, String password) {
//        try {
        // Checking if user shouldn't be locked
        checkIfUserShouldntBeLocked(username);
        // Checking if User is locked and first failed attempt is in the last 10 minutes then no authorization is happening
        if (checkIfUserIsNotLocked(username)){
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            loginAttemptSuccededClearing(username);
        } else throw new RuntimeException("User with username of: " + username + " is locked for 10 minutes because of failing attempts.");
//        secureUserFromBruteForceAttack(username);
    }

    private void checkIfUserShouldntBeLocked(String username) {
        if (!checkIfUserIsNotLocked(username) && firstFailedAttemptIn10Minutes(username)){
            User existingUser = findUserByUsername(username);
            existingUser.setIs_not_locked(true);
            // We define it 0 so that the next time the user will fail he wont be instantly locked for another 10 minutes but he will have another 5 chances
            existingUser.setNum_of_attempts(0);
            userRepository.save(existingUser);
        }
    }

    private boolean firstFailedAttemptIn10Minutes(String username) {
        User existingUser = findUserByUsername(username);
        return DateUtils.addMinutes(existingUser.getFirst_failed_attempt_time(), 10).compareTo(new Date()) < 0;
    }

    // TODO: 8/12/2020 Test
    // Setting user's params for brute force attack as negative
    public void loginAttemptSuccededClearing(String username) {
        User existingUser = findUserByUsername(username);
        existingUser.setNum_of_attempts(0);
        existingUser.setLast_attempt_succeed(true);
        existingUser.setFirst_failed_attempt_time(null);
        userRepository.save(existingUser);
    }

    // TODO: 8/12/2020 Test
    // Brute force securing the system from the User
    public void secureUserFromBruteForceAttack(String username) {
        User existingUser = findUserByUsername(username);
        existingUser.setLast_attempt_succeed(false);
        // Locking the user if number of failed attempts is 5 and first failed attempt is less than 10 minutes
        if (existingUser.getNum_of_attempts() >= 5
                && DateUtils.addMinutes(existingUser.getFirst_failed_attempt_time(), 10).compareTo(new Date()) < 0) existingUser.setIs_not_locked(false);
        // If first failed attempt time is null Or first failed attempt time is more than 10 minutes from now
        if (existingUser.getFirst_failed_attempt_time() == null) {
            existingUser.setIs_not_locked(true);
            existingUser.setNum_of_attempts(existingUser.getNum_of_attempts() + 1);
            existingUser.setFirst_failed_attempt_time(new Date());
        }
        // If first failed attempt isn't null and first failed attempt is in the last 10 minutes from now and num_attempts isn't >= 5
        else if ( DateUtils.addMinutes(existingUser.getFirst_failed_attempt_time(), 10).compareTo(new Date()) > 0) {
            existingUser.setIs_not_locked(true);
            existingUser.setNum_of_attempts(existingUser.getNum_of_attempts() + 1);
        }
        // TODO: 8/15/2020 DOESN'T WORK -> I AM SAVING, JPA RETURNS CORRECT DATA BUT IN THE DB ACTUALLY NOTHING HAS CHANGED!
        User returnedUser = userRepository.save(existingUser);
    }

    // TODO: 8/12/2020 Test
    private boolean checkIfUserIsNotLocked(String username) {
        // Does the checks
        // TODO: 8/14/2020 I have to check here if it is in the last 15 minutes , if not then i should return not locked being false to try again to attempt for logins
        User existingUser = findUserByUsername(username);

        return existingUser.getIs_not_locked();
    }

    private HttpHeaders getJwtHeader(User existingUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(existingUser));
        return headers;
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


    // TODO: 8/12/2020 Check where it is being used probably from spring security
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

    // TODO: 8/11/2020 --How to make it SECURE knowing surely that the user that triggers the request is actually the one that asks the request and not someone else that by-passed (got it somehow) JWT token. --Could be with actually checking the token de-encrypting it to see the actual username (Learn how to do this also manually (Spring security does it as well))
    @Override
    public User patchUser(UserDTO userDTO) {
        // Throwing exceptions in the method
        User existingUser = findUserByUsername(userDTO.getUsername());
        if (checkIfEditIsBySameUser(userDTO.getUsername(), existingUser)){
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
        }
        return userRepository.save(existingUser);
    }

    // Checking if usernames match
    private boolean checkIfEditIsBySameUser(String username, User existingUser) {
        return username.equals(existingUser.getUsername());
    }

    // TODO: 8/11/2020 Find a better approach
    @Override
    public User patchUsernameOfUser(UserDTO userDTO) {
        // Does all the checks-exceptions inside
        User existingUser = findUserByEmail(userDTO.getEmail());
        if (StringUtils.isNotBlank(userDTO.getUsername()) && StringUtils.isNotEmpty(userDTO.getUsername()) && checkUserByUsername(userDTO.getUsername()) != null){
            existingUser.setEmail(userDTO.getEmail());
            return existingUser;
        } else throw new RuntimeException("Username param was either empty, blank or User didn't exist in the db with such.");
    }

    @Override
    public Todo addNewTodo(TodoDTO todoDTO) {
        User existingUser = findUserByUsername(todoDTO.getUsername());
        Todo newTodo = createNewTodo(todoDTO);
        existingUser.addTodo(newTodo);
        userRepository.save(existingUser);
        Optional<Todo> optionalTodo = todoRepository.findByUuid(newTodo.getUuid());
        if (optionalTodo.isPresent()) return optionalTodo.get();
        else throw new RuntimeException("There was a problem trying to save the new Todo with uuid of: " + newTodo.getUuid() + ".");
    }

    @Override
    public Todo patchTodo(TodoDTO todoDTO) {
        User existingUser = findUserByUsername(todoDTO.getUsername());
        Todo existingTodo = todoServiceImpl.findTodoById(todoDTO.getId());
        if (existingTodo.getUser().getId().equals(existingUser.getId())){
            if (todoDTO.getTitle() != null) existingTodo.setTitle(todoDTO.getTitle());
            if (todoDTO.getDescription() != null) existingTodo.setDescription(todoDTO.getDescription());
            if (todoDTO.getDate_deadline() != null) existingTodo.setDate_deadline(todoDTO.getDate_deadline());
            return todoRepository.save(existingTodo);
        } else throw new RuntimeException("Todo with id of: " + todoDTO.getId() + " is not connected with User with id of: " + existingUser.getId());
    }

    private Todo createNewTodo(TodoDTO todoDTO) {
        Todo newTodo = new Todo();
        newTodo.setTitle(todoDTO.getTitle());
        newTodo.setDescription(todoDTO.getDescription());
        newTodo.setDate_deadline(todoDTO.getDate_deadline());
        newTodo.setFinished(todoDTO.isFinished());
        newTodo.setUuid(RandomStringUtils.randomAlphanumeric(14));
        return newTodo;
    }

}
