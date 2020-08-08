package com.markovic.todoApplication.domain;

import javax.persistence.*;

@Entity
public class Stigma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    private String ip;

    private String location;

    private String device_name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Stigma() {
    }

    public Stigma(Long id, String ip, String location, String device_name, User user) {
        this.id = id;
        this.ip = ip;
        this.location = location;
        this.device_name = device_name;
        this.user = user;
    }

    public Stigma(String ip) {
        this.ip = ip;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
