package com.markovic.todoApplication.domain;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    // TODO: 7/27/2020 If it doesn't work i can user RandomStringUtils.randomNumeric(10) to generate a String of 10 numeric values using the library we have installed
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="uuid-char")
    @Column(name = "uuid", columnDefinition = "VARCHAR(255)", nullable = false, unique = true, updatable = false)
    private String uuid;

    private String title;

    private String description;

    private Date date_deadline;

    private boolean finished;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Todo() {
    }

    public Todo(Long id, String uuid, String title, String description, Date date_deadline, boolean finished, User user) {
        this.id = id;
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.date_deadline = date_deadline;
        this.finished = finished;
        this.user = user;
    }

    public long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getUuid() { return uuid; }

    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate_deadline() {
        return date_deadline;
    }

    public void setDate_deadline(Date date_deadline) {
        this.date_deadline = date_deadline;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
