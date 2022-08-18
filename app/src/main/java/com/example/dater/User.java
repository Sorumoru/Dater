package com.example.dater;

public class User {
    public String username;
    public String email;

    public User() {
    }

    public User (String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
