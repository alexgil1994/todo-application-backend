package com.markovic.todoApplication.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    // Can't be generated automatically by spring because we already use @Id for the id. Could be done if we used as main id column the uuid.
    @Column(name = "uuid", nullable = false, unique = true)
    private String uuid;

    @Column(name = "first_name", nullable = false)
    private String first_name;

    @Column(name = "last_name", nullable = false)
    private String last_name;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    // TODO: 7/27/2020 Could use a random password if needed (for example for when resetting the password to send it in the email) using RandomStringUtils.randomAlphanumeric(10)
    @Column(name = "password", nullable = false)
    private String password;

    private String image_url;

    private String background_image_url;

    private String theme;

    private Date added_date;

    private Date last_login_date;

    private Date last_login_date_display;

    private boolean is_enabled;

    private boolean is_not_locked;

    private String user_role;

    private String[] user_authorities;

    // For Brute force attack purposes
    private int num_of_attempts;
    private Date first_failed_attempt_time;
    private boolean last_attempt_succeed;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private Set<Todo> todoSet = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private Set<Stigma> stigmaSet = new HashSet<>();

    // Basic methods
    public void addTodo(Todo todo){
        todo.setUser(this);
        this.todoSet.add(todo);
    }

    public void removeTodo(Todo todo){
        todo.setUser(null);
        this.todoSet.remove(todo);
    }

    // Basic methods
    public void addStigma(Stigma stigma){
        stigma.setUser(this);
        this.stigmaSet.add(stigma);
    }

    public void removeStigma(Stigma stigma){
        stigma.setUser(null);
        this.stigmaSet.remove(stigma);
    }

    public User() {
    }

    public User(Long id, String uuid, String first_name, String last_name, String username, String email, String password, String image_url, String background_image_url, String theme, Date added_date, Date last_login_date, Date last_login_date_display, Boolean is_enabled, Boolean is_not_locked, String user_role, String[] user_authorities, Set<Todo> todoSet, Set<Stigma> stigmaSet, int num_of_attempts, Date first_failed_attempt_time, boolean last_attempt_succeed) {
        this.id = id;
        this.uuid = uuid;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.image_url = image_url;
        this.background_image_url = background_image_url;
        this.theme = theme;
        this.added_date = added_date;
        this.last_login_date = last_login_date;
        this.last_login_date_display = last_login_date_display;
        this.is_enabled = is_enabled;
        this.is_not_locked = is_not_locked;
        this.user_role = user_role;
        this.user_authorities = user_authorities;
        this.todoSet = todoSet;
        this.stigmaSet = stigmaSet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    // We are not using these 3 in our app logic but needed from UserDetails and being used from jwt authentication. We use different vars for brute force attack protection
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return is_not_locked;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return is_enabled;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return stream(getUser_authorities()).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getBackground_image_url() {
        return background_image_url;
    }

    public void setBackground_image_url(String background_image_url) {
        this.background_image_url = background_image_url;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Date getAdded_date() {
        return added_date;
    }

    public void setAdded_date(Date added_date) {
        this.added_date = added_date;
    }

    public Date getLast_login_date() {
        return last_login_date;
    }

    public void setLast_login_date(Date last_login_date) {
        this.last_login_date = last_login_date;
    }

    public Date getLast_login_date_display() {
        return last_login_date_display;
    }

    public void setLast_login_date_display(Date last_login_date_display) {
        this.last_login_date_display = last_login_date_display;
    }

    public boolean getIs_enabled() {
        return is_enabled;
    }

    public void setIs_enabled(boolean is_enabled) {
        this.is_enabled = is_enabled;
    }

    public boolean getIs_not_locked() {
        return is_not_locked;
    }

    public void setIs_not_locked(boolean is_not_locked) {
        this.is_not_locked = is_not_locked;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String[] getUser_authorities() {
        return user_authorities;
    }

    public void setUser_authorities(String[] user_authorities) {
        this.user_authorities = user_authorities;
    }

    public int getNum_of_attempts() { return num_of_attempts; }

    public void setNum_of_attempts(int num_of_attempts) { this.num_of_attempts = num_of_attempts; }

    public Date getFirst_failed_attempt_time() { return first_failed_attempt_time; }

    public void setFirst_failed_attempt_time(Date first_failed_attempt_time) { this.first_failed_attempt_time = first_failed_attempt_time; }

    public boolean isLast_attempt_succeed() { return last_attempt_succeed; }

    public void setLast_attempt_succeed(boolean last_attempt_succeed) { this.last_attempt_succeed = last_attempt_succeed; }

    public Set<Todo> getTodoSet() {
        return todoSet;
    }

    public void setTodoSet(Set<Todo> todoSet) {
        this.todoSet = todoSet;
    }

    public Set<Stigma> getStigmaSet() {
        return stigmaSet;
    }

    public void setStigmaSet(Set<Stigma> stigmaSet) {
        this.stigmaSet = stigmaSet;
    }
}
