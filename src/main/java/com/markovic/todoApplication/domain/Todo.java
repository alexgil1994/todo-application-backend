package com.markovic.todoApplication.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String title;

    private String description;

    private Date date_deadline;

    private boolean finished;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    public Todo() {
    }

    public Todo(Long id, String title, String description, Date date_deadline, boolean finished, Profile profile) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date_deadline = date_deadline;
        this.finished = finished;
        this.profile = profile;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
