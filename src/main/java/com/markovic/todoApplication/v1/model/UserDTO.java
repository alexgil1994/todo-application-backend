package com.markovic.todoApplication.v1.model;

public class UserDTO {

    private String first_name;

    private String last_name;

    private String username;

    private String email;

    private String password;

    private String ip;

    private String image_url;

    private String background_image_url;

    private String theme;

    public UserDTO() {
    }

    public UserDTO(String first_name, String last_name, String username, String email, String password, String ip, String image_url, String background_image_url, String theme) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.ip = ip;
        this.image_url = image_url;
        this.background_image_url = background_image_url;
        this.theme = theme;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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
}
