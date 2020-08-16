package com.markovic.todoApplication.v1.model;

public class UpdatePasswordUserDTO {
    private String username;
    private String email;
    private String old_password;
    private String new_password;

    public UpdatePasswordUserDTO() {
    }

    public UpdatePasswordUserDTO(String username, String email, String old_password, String new_password) {
        this.username = username;
        this.email = email;
        this.old_password = old_password;
        this.new_password = new_password;
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

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }
}
