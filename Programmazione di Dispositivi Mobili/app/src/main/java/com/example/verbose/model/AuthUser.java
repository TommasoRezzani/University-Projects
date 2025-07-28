package com.example.verbose.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthUser extends BaseUser {
    @SerializedName("complete_profile")
    private boolean completeProfile;
    Set<Category> preferences;

    @Deprecated
    public AuthUser(String uid, String email) {
        super(uid, email);
        this.completeProfile = false;
        this.preferences = new HashSet<>();
    }

    public AuthUser(String email, String username, String firstName, String lastName, String bio, Set<Category> preferences) {
        super(email, username, firstName, lastName, bio);
        this.completeProfile = false;
        this.preferences = preferences;
    }
    public boolean isCompleteProfile() {
        return completeProfile;
    }
    public Set<Category> getPreferences() {
        return preferences;
    }

    @Deprecated
    public Set<String> getPreferencesNames(){
        return preferences.stream().map(Category::getName).collect(Collectors.toSet());
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setPreferences(Set<Category> preferences) {
        this.preferences = preferences;
    }

    public void addPreferences(Set<Category> preferences) {
        this.preferences.addAll(preferences);
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "Uid='" + getUid() + '\'' +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", bio='" + bio + '\'' +
                ", completeAccount=" + completeProfile +
                '}';
    }
}
