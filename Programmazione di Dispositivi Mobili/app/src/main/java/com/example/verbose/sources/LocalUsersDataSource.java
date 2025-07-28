package com.example.verbose.sources;

import com.example.verbose.model.AuthUser;
import com.example.verbose.model.Token;
import com.example.verbose.util.ServiceLocator;

import java.util.Optional;

/**
 * Memorizza i dati dell'utente attualmente loggato
 * e il relativo token per richieste all'API
 */

public class LocalUsersDataSource {
    private static AuthUser authUser;
    private static Token token;
    private static String registrationId;
    private static String deviceId;

    public LocalUsersDataSource(){
    }

    public synchronized AuthUser getLoggedUser(){
        return authUser;
    }

    public synchronized boolean hasUser(){
        return authUser != null;
    }

    public synchronized boolean hasUser(String id){
        return hasUser() && authUser.getUid().equals(id);
    }

    public synchronized void saveCurrentUserData(AuthUser authUser){
        LocalUsersDataSource.authUser = authUser;
    }

    public synchronized void setToken(Token token){
        LocalUsersDataSource.token = token;
    }

    public synchronized Optional<Token> getToken() {
        return Optional.ofNullable(token);
    }

    public synchronized void setDeviceId(String deviceId){
        LocalUsersDataSource.deviceId = deviceId;
    }

    public synchronized void setRegistrationId(String registration_id) {
        LocalUsersDataSource.registrationId = registration_id;
    }

    public synchronized Optional<String> getDeviceId(){
        return Optional.ofNullable(deviceId);
    }

    public synchronized Optional<String> getRegistrationId(){
        return Optional.ofNullable(registrationId);
    }

    public synchronized void logout(){
        LocalUsersDataSource.authUser = null;
        LocalUsersDataSource.token = null;
        LocalUsersDataSource.registrationId = null;
    }


}
