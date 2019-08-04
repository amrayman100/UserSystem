package com.sumerge.program.viewmodels;

public class UserModel {
    private String name;
    private String username;
    private String role;
    private String password;
    private String email;
    private String number;

    public UserModel(String name, String username, String role, String password, String email, String number) {
        this.name = name;
        this.username = username;
        this.role = role;
        this.password = password;
        this.email = email;
        this.number = number;
    }

    public UserModel(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
