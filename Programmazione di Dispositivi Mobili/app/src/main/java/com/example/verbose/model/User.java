package com.example.verbose.model;

public class User extends BaseUser {

    public User(String Uid, String email) {
        super(Uid, email);
    }

    public User(String email, String username, String firstName, String lastName, String bio) {
        super(email, username, firstName, lastName, bio);
    }
}
