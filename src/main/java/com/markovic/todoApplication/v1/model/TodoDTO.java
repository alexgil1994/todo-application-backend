package com.markovic.todoApplication.v1.model;

import java.util.Date;

public class TodoDTO {

    private String username;
    private Long id;
    private String uuid;
    private String title;
    private String description;
    private Date date_deadline;
    private boolean finished;

    public TodoDTO() {
    }

    public TodoDTO(String title, Long id, String uuid, String description, Date date_deadline, boolean finished) {
        this.id = id;
        this.uuid = uuid;
        this.title = title;
        this.description = description;
        this.date_deadline = date_deadline;
        this.finished = finished;
    }

    public TodoDTO(String username, Long id, String uuid, String title, String description, Date date_deadline, boolean finished) {
        this.id = id;
        this.uuid = uuid;
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
