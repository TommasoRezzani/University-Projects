package com.example.verbose.model;

public class Token {
    private final String idToken;
    private final long expirationTime;

    public Token(String idToken, long expirationTime){
        this.idToken = idToken;
        this.expirationTime = expirationTime;
    }

    public String getIdToken() {
        return idToken;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expirationTime - 5000;
    }
}
