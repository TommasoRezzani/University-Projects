package com.example.verbose.model;

import com.example.verbose.Constants;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public abstract class BaseUser implements Serializable {
    @SerializedName("firebaseId")
    private String Uid;
    protected String email;
    protected String username;
    @SerializedName("first_name")
    protected String firstName;
    @SerializedName("last_name")
    protected String lastName;
    protected String bio;
    @SerializedName("picture")
    protected String profilePicture;

    public BaseUser(String Uid, String email){
        this.Uid = Uid;
        this.email = email;
    }

    public BaseUser(String email, String username, String firstName, String lastName, String bio) {
        Uid = null;
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
    }

    public String getUid() {
        return Uid;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBio() {
        return bio;
    }

    public String getProfilePicture() {
        if(profilePicture == null || profilePicture.startsWith("http"))
            return profilePicture;
        else
            return Constants.SERVER_BASE_URL + profilePicture;
    }
}
