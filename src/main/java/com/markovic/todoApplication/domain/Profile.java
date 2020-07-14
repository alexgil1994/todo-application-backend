package com.markovic.todoApplication.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String username;

    private String email;

    private String password;

    private String password2;

    private Date date_added;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profile")
    @JsonIgnore
    private Set<Todo> todoSet = new HashSet<>();

    public void addTodo(Todo todo){
        todo.setProfile(this);
        this.todoSet.add(todo);
    }

    public void removeTodo(Todo todo){
        todo.setProfile(null);
        this.todoSet.remove(todo);
    }

    public Profile() {
    }

    public Profile(Long id, String username, String email, String password, String password2, Date date_added, Set<Todo> todoSet) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.password2 = password2;
        this.date_added = date_added;
        this.todoSet = todoSet;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public Date getDate_added() {
        return date_added;
    }

    public void setDate_added(Date date_added) {
        this.date_added = date_added;
    }

    public Set<Todo> getTodoSet() {
        return todoSet;
    }

    public void setTodoSet(Set<Todo> todoSet) {
        this.todoSet = todoSet;
    }
}
