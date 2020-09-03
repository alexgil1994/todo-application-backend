package com.markovic.todoApplication.v1.model;

public class ResetPasswordUserDTO {
    private String username;
    private String email;

    public ResetPasswordUserDTO() {
    }

    public ResetPasswordUserDTO(String username, String email) {
        this.username = username;
        this.email = email;
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
}
