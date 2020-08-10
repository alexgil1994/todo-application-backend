package com.markovic.todoApplication.v1.model;

import java.util.Date;

public class TodoDTO {

    private String username;
    private String title;
    private String description;
    private Date date_deadline;
    private boolean finished;

    public TodoDTO() {
    }

    public TodoDTO(String title, String description, Date date_deadline, boolean finished) {
        this.title = title;
        this.description = description;
        this.date_deadline = date_deadline;
        this.finished = finished;
    }

    public TodoDTO(String username, String title, String description, Date date_deadline, boolean finished) {
        this.username = username;
        this.title = title;
        this.description = description;
        this.date_deadline = date_deadline;
        this.finished = finished;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
