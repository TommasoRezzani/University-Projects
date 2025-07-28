package com.example.verbose.repository;

import android.util.Log;

import com.example.verbose.model.Appointment;
import com.example.verbose.model.AuthUser;
import com.example.verbose.model.Repetition;
import com.example.verbose.model.Token;
import com.example.verbose.sources.IAppointmentsCallback;
import com.example.verbose.sources.IRepetitionsCallback;
import com.example.verbose.sources.IReviewsCallback;
import com.example.verbose.sources.ITokenCallback;
import com.example.verbose.sources.IUsernameCallback;
import com.example.verbose.sources.IUsersCallback;
import com.example.verbose.sources.LocalUsersDataSource;
import com.example.verbose.sources.RestAppointmentsDataSource;
import com.example.verbose.sources.RestRepetitionsDataSource;
import com.example.verbose.sources.RestUsersDataSource;

import java.io.File;

public class UsersRepository {
    private static final String TAG = UsersRepository.class.getSimpleName();

    private final RestUsersDataSource restUsersDataSource;
    private final LocalUsersDataSource localUsersDataSource;
    private final RestRepetitionsDataSource restRepetitionsDataSource;
    private final RestAppointmentsDataSource restAppointmentsDataSource;

    public UsersRepository(){
        restUsersDataSource = new RestUsersDataSource();
        localUsersDataSource = new LocalUsersDataSource();
        restRepetitionsDataSource = new RestRepetitionsDataSource();
        restAppointmentsDataSource = new RestAppointmentsDataSource();
    }

    public void signIn(String idToken, IUsersCallback callback){
        this.restUsersDataSource.signInWithGoogle(idToken, new IUsersCallback() {
            @Override
            public void onSuccess(AuthUser authUser) {
                if(authUser != null) {
                    localUsersDataSource.saveCurrentUserData(authUser);
                    callback.onSuccess(authUser);
                } else {
                    callback.onFailure(new Throwable("Google account not registered"));
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    public void signIn(String email, String password, IUsersCallback callback){
        this.restUsersDataSource.signInWithCredentials(email, password, new IUsersCallback() {
            @Override
            public void onSuccess(AuthUser authUser) {
                localUsersDataSource.saveCurrentUserData(authUser);
                callback.onSuccess(authUser);
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    public void signUp(String idToken, IUsersCallback callback){
        this.restUsersDataSource.createUser(idToken, new IUsersCallback() {
            @Override
            public void onSuccess(AuthUser authUser) {
                localUsersDataSource.saveCurrentUserData(authUser);
                callback.onSuccess(authUser);
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    public void signUp(String email, String password, IUsersCallback callback){
        this.restUsersDataSource.createUser(email, password, new IUsersCallback() {
            @Override
            public void onSuccess(AuthUser authUser) {
                localUsersDataSource.saveCurrentUserData(authUser);
                callback.onSuccess(authUser);
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    public void getLoggedUserData(IUsersCallback callback){
        if(this.localUsersDataSource.hasUser()){
            callback.onSuccess(this.localUsersDataSource.getLoggedUser());
        }else {
            this.restUsersDataSource.getLoggedUserData(new IUsersCallback() {
                @Override
                public void onSuccess(AuthUser authUser) {
                    localUsersDataSource.saveCurrentUserData(authUser);
                    callback.onSuccess(authUser);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        }
    }

    public void getUserData(String id, IUsersCallback callback){
        if(this.localUsersDataSource.hasUser(id)){
            callback.onSuccess(this.localUsersDataSource.getLoggedUser());
        }else {
            this.restUsersDataSource.getUserData(id, callback);
        }
    }

    public void updateUserRegistrationToken(String deviceId,String registrationId){
        assert this.localUsersDataSource.hasUser();

        this.localUsersDataSource.setDeviceId(deviceId);
        this.localUsersDataSource.setRegistrationId(registrationId);

        if(!this.localUsersDataSource.getToken().isPresent() || this.localUsersDataSource.getToken().get().isExpired()){
            this.restUsersDataSource.refreshToken(new ITokenCallback() {
                @Override
                public void onSuccess(Token token) {
                    localUsersDataSource.setToken(token);
                    restUsersDataSource.updateRegistrationId(token.getIdToken(), localUsersDataSource.getLoggedUser().getUid(), deviceId,registrationId);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Log.e(TAG, throwable.toString());
                }
            });
        }else {
            this.restUsersDataSource.updateRegistrationId(this.localUsersDataSource.getToken().get().getIdToken(), localUsersDataSource.getLoggedUser().getUid(), deviceId,registrationId);
        }
    }

    public void deleteUserRegistrationToken(){
        assert this.localUsersDataSource.hasUser();
        assert this.localUsersDataSource.getDeviceId().isPresent();
        assert this.localUsersDataSource.getRegistrationId().isPresent();

        if(!this.localUsersDataSource.getToken().isPresent() || this.localUsersDataSource.getToken().get().isExpired()){
            this.restUsersDataSource.refreshToken(new ITokenCallback() {
                @Override
                public void onSuccess(Token token) {
                    localUsersDataSource.setToken(token);
                    restUsersDataSource.deleteRegistrationId(
                            token.getIdToken(),
                            localUsersDataSource.getLoggedUser().getUid(),
                            localUsersDataSource.getDeviceId().get());
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Log.e(TAG, throwable.toString());
                }
            });
        }else {
            this.restUsersDataSource.deleteRegistrationId(
                    this.localUsersDataSource.getToken().get().getIdToken(),
                    localUsersDataSource.getLoggedUser().getUid(),
                    localUsersDataSource.getDeviceId().get());
        }
    }

    public void updateUserData(AuthUser authUser, IUsersCallback callback){
        IUsersCallback utilCallback = new IUsersCallback() {
            @Override
            public void onSuccess(AuthUser user) {
                localUsersDataSource.saveCurrentUserData(user);
                callback.onSuccess(user);
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }
        };
        assert this.localUsersDataSource.hasUser(); // Deve esistere un utente loggato da modificare

        if(!this.localUsersDataSource.getToken().isPresent() || this.localUsersDataSource.getToken().get().isExpired()){
            this.restUsersDataSource.refreshToken(new ITokenCallback() {
                @Override
                public void onSuccess(Token token) {
                    localUsersDataSource.setToken(token);
                    restUsersDataSource.updateUserData(token.getIdToken(), authUser, utilCallback);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        }else {
            this.restUsersDataSource.updateUserData(this.localUsersDataSource.getToken().get().getIdToken(), authUser, utilCallback);
        }
    }

    public void updateProfilePic(File picture, IUsersCallback callback){
        assert this.localUsersDataSource.hasUser();

        IUsersCallback utilCallback = new IUsersCallback() {
            @Override
            public void onSuccess(AuthUser user) {
                localUsersDataSource.saveCurrentUserData(user);
                callback.onSuccess(user);
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }
        };
        assert this.localUsersDataSource.hasUser(); // Deve esistere un utente loggato da modificare

        if(!this.localUsersDataSource.getToken().isPresent() || this.localUsersDataSource.getToken().get().isExpired()){
            this.restUsersDataSource.refreshToken(new ITokenCallback() {
                @Override
                public void onSuccess(Token token) {
                    localUsersDataSource.setToken(token);
                    restUsersDataSource.updateProfilePic(
                            token.getIdToken(),
                            localUsersDataSource.getLoggedUser().getUid(),
                            picture,
                            utilCallback);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        }else {
            this.restUsersDataSource.updateProfilePic(
                    this.localUsersDataSource.getToken().get().getIdToken(),
                    localUsersDataSource.getLoggedUser().getUid(),
                    picture,
                    utilCallback);
        }
    }

    public void getUserRepetitions(IRepetitionsCallback callback){
        assert this.localUsersDataSource.hasUser();

        if(!this.localUsersDataSource.getToken().isPresent() || this.localUsersDataSource.getToken().get().isExpired()){
            this.restUsersDataSource.refreshToken(new ITokenCallback() {
                @Override
                public void onSuccess(Token token) {
                    localUsersDataSource.setToken(token);
                    restUsersDataSource.getUserRepetitions(
                            token.getIdToken(),
                            localUsersDataSource.getLoggedUser().getUid(),
                            callback);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        }
        else{
            this.restUsersDataSource.getUserRepetitions(
                    this.localUsersDataSource.getToken().get().getIdToken(),
                    this.localUsersDataSource.getLoggedUser().getUid(),
                    callback);
        }
    }

    public void createUserRepetition(Repetition repetition, IRepetitionsCallback callback){
        assert this.localUsersDataSource.hasUser();;

        if(!this.localUsersDataSource.getToken().isPresent() || this.localUsersDataSource.getToken().get().isExpired()){
            this.restUsersDataSource.refreshToken(new ITokenCallback() {
                @Override
                public void onSuccess(Token token) {
                    localUsersDataSource.setToken(token);
                    restRepetitionsDataSource.createRepetition(token.getIdToken(), repetition, callback);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        }else {
            this.restRepetitionsDataSource.createRepetition(this.localUsersDataSource.getToken().get().getIdToken(), repetition, callback);
        }
    }

    public void updateUserRepetition(Repetition repetition, IRepetitionsCallback callback){
        assert this.localUsersDataSource.hasUser();

        if(!this.localUsersDataSource.getToken().isPresent() || this.localUsersDataSource.getToken().get().isExpired()){
            this.restUsersDataSource.refreshToken(new ITokenCallback() {
                @Override
                public void onSuccess(Token token) {
                    localUsersDataSource.setToken(token);
                    restRepetitionsDataSource.updateRepetition(token.getIdToken(), repetition, callback);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        }else {
            this.restRepetitionsDataSource.updateRepetition(this.localUsersDataSource.getToken().get().getIdToken(), repetition, callback);
        }
    }

    public void getUserPendingRequests(IAppointmentsCallback callback){
        assert this.localUsersDataSource.hasUser();

        if(!this.localUsersDataSource.getToken().isPresent() || this.localUsersDataSource.getToken().get().isExpired()){
            this.restUsersDataSource.refreshToken(new ITokenCallback() {
                @Override
                public void onSuccess(Token token) {
                    localUsersDataSource.setToken(token);
                    restUsersDataSource.getUserPendingRequests(
                            token.getIdToken(),
                            localUsersDataSource.getLoggedUser().getUid(),
                            callback);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        }else{
            this.restUsersDataSource.getUserPendingRequests(
                    this.localUsersDataSource.getToken().get().getIdToken(),
                    this.localUsersDataSource.getLoggedUser().getUid(),
                    callback
            );
        }
    }

    public void getUserAcceptedRequests(IAppointmentsCallback callback){
        assert this.localUsersDataSource.hasUser();

        if(!this.localUsersDataSource.getToken().isPresent() || this.localUsersDataSource.getToken().get().isExpired()){
            this.restUsersDataSource.refreshToken(new ITokenCallback() {
                @Override
                public void onSuccess(Token token) {
                    localUsersDataSource.setToken(token);
                    restUsersDataSource.getUserAcceptedRequests(
                            token.getIdToken(),
                            localUsersDataSource.getLoggedUser().getUid(),
                            callback);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        }else{
            this.restUsersDataSource.getUserAcceptedRequests(
                    this.localUsersDataSource.getToken().get().getIdToken(),
                    this.localUsersDataSource.getLoggedUser().getUid(),
                    callback
            );
        }
    }

    public void getUserClosedRequests(IAppointmentsCallback callback){
        assert this.localUsersDataSource.hasUser();

        if(!this.localUsersDataSource.getToken().isPresent() || this.localUsersDataSource.getToken().get().isExpired()){
            this.restUsersDataSource.refreshToken(new ITokenCallback() {
                @Override
                public void onSuccess(Token token) {
                    localUsersDataSource.setToken(token);
                    restUsersDataSource.getUserClosedRequests(
                            token.getIdToken(),
                            localUsersDataSource.getLoggedUser().getUid(),
                            callback);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        }else{
            this.restUsersDataSource.getUserClosedRequests(
                    this.localUsersDataSource.getToken().get().getIdToken(),
                    this.localUsersDataSource.getLoggedUser().getUid(),
                    callback
            );
        }
    }

    public void getUserOpenAppointments(IAppointmentsCallback callback){
        assert this.localUsersDataSource.hasUser();

        if(!this.localUsersDataSource.getToken().isPresent() || this.localUsersDataSource.getToken().get().isExpired()){
            this.restUsersDataSource.refreshToken(new ITokenCallback() {
                @Override
                public void onSuccess(Token token) {
                    localUsersDataSource.setToken(token);
                    restUsersDataSource.getUserOpenAppointments(
                            token.getIdToken(),
                            localUsersDataSource.getLoggedUser().getUid(),
                            callback);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        }else{
            this.restUsersDataSource.getUserOpenAppointments(
                    this.localUsersDataSource.getToken().get().getIdToken(),
                    this.localUsersDataSource.getLoggedUser().getUid(),
                    callback
            );
        }
    }

    public void addUserAppointment(Appointment appointment, IAppointmentsCallback callback){
        assert this.localUsersDataSource.hasUser();

        if(!this.localUsersDataSource.getToken().isPresent() || this.localUsersDataSource.getToken().get().isExpired()){
            this.restUsersDataSource.refreshToken(new ITokenCallback() {
                @Override
                public void onSuccess(Token token) {
                    localUsersDataSource.setToken(token);
                    restAppointmentsDataSource.createAppointment(token.getIdToken(), appointment, callback);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        }else {
            this.restAppointmentsDataSource.createAppointment(this.localUsersDataSource.getToken().get().getIdToken(), appointment, callback);
        }
    }

    public void acceptUserRequest(String appointmentId, IAppointmentsCallback callback){
        assert this.localUsersDataSource.hasUser();

        if(!this.localUsersDataSource.getToken().isPresent() || this.localUsersDataSource.getToken().get().isExpired()){
            this.restUsersDataSource.refreshToken(new ITokenCallback() {
                @Override
                public void onSuccess(Token token) {
                    localUsersDataSource.setToken(token);
                    restAppointmentsDataSource.acceptAppointment(token.getIdToken(), appointmentId, callback);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        }else {
            this.restAppointmentsDataSource.acceptAppointment(this.localUsersDataSource.getToken().get().getIdToken(), appointmentId, callback);
        }
    }

    public void refuseUserRequest(String appointmentId, IAppointmentsCallback callback){
        assert this.localUsersDataSource.hasUser();

        if(!this.localUsersDataSource.getToken().isPresent() || this.localUsersDataSource.getToken().get().isExpired()){
            this.restUsersDataSource.refreshToken(new ITokenCallback() {
                @Override
                public void onSuccess(Token token) {
                    localUsersDataSource.setToken(token);
                    restAppointmentsDataSource.refuseAppointment(token.getIdToken(), appointmentId, callback);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        }else {
            this.restAppointmentsDataSource.refuseAppointment(this.localUsersDataSource.getToken().get().getIdToken(), appointmentId, callback);
        }
    }

    public void closeAppointment(Appointment appointment, IAppointmentsCallback callback){
        assert this.localUsersDataSource.hasUser();

        if(!this.localUsersDataSource.getToken().isPresent() || this.localUsersDataSource.getToken().get().isExpired()){
            this.restUsersDataSource.refreshToken(new ITokenCallback() {
                @Override
                public void onSuccess(Token token) {
                    localUsersDataSource.setToken(token);
                    restAppointmentsDataSource.closeAppointment(token.getIdToken(), appointment.getId(), callback);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        }else {
            this.restAppointmentsDataSource.closeAppointment(this.localUsersDataSource.getToken().get().getIdToken(), appointment.getId(), callback);
        }
    }

    public void deleteUserAppointment(Appointment appointment){
        assert this.localUsersDataSource.hasUser();

        if(!this.localUsersDataSource.getToken().isPresent() || this.localUsersDataSource.getToken().get().isExpired()){
            this.restUsersDataSource.refreshToken(new ITokenCallback() {
                @Override
                public void onSuccess(Token token) {
                    localUsersDataSource.setToken(token);
                    restAppointmentsDataSource.deleteAppointment(token.getIdToken(), appointment);
                }

                @Override
                public void onFailure(Throwable throwable) {
                }
            });
        }else {
            this.restAppointmentsDataSource.deleteAppointment(this.localUsersDataSource.getToken().get().getIdToken(), appointment);
        }
    }

    public void getUserNotifications(IAppointmentsCallback callback){
        assert this.localUsersDataSource.hasUser();

        if(!this.localUsersDataSource.getToken().isPresent() || this.localUsersDataSource.getToken().get().isExpired()){
            this.restUsersDataSource.refreshToken(new ITokenCallback() {
                @Override
                public void onSuccess(Token token) {
                    localUsersDataSource.setToken(token);
                    restUsersDataSource.getUserNotifications(
                            token.getIdToken(),
                            localUsersDataSource.getLoggedUser().getUid(),
                            callback);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        }else{
            restUsersDataSource.getUserNotifications(
                    this.localUsersDataSource.getToken().get().getIdToken(),
                    localUsersDataSource.getLoggedUser().getUid(),
                    callback);
        }
    }

    public void getUserFavorites(IRepetitionsCallback callback){
        assert this.localUsersDataSource.hasUser();

        if(!this.localUsersDataSource.getToken().isPresent() || this.localUsersDataSource.getToken().get().isExpired()){
            this.restUsersDataSource.refreshToken(new ITokenCallback() {
                @Override
                public void onSuccess(Token token) {
                    localUsersDataSource.setToken(token);
                    restUsersDataSource.getUserFavorites(
                            token.getIdToken(),
                            localUsersDataSource.getLoggedUser().getUid(),
                            callback);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        }else{
            restUsersDataSource.getUserFavorites(
                    this.localUsersDataSource.getToken().get().getIdToken(),
                    localUsersDataSource.getLoggedUser().getUid(),
                    callback);
        }
    }

    public void getUserReviews(IReviewsCallback callback){
        restUsersDataSource.getUserReviews(
                localUsersDataSource.getLoggedUser().getUid(),
                callback);
    }

    public void isUsernameFree(String username, IUsernameCallback callback){
        this.restUsersDataSource.isUsernameFree(username, new IUsernameCallback() {
            @Override
            public void onSuccess(Boolean result) {
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    public void logout(IUsersCallback callback){
        assert this.localUsersDataSource.hasUser();

        if(!this.localUsersDataSource.getToken().isPresent() || this.localUsersDataSource.getToken().get().isExpired()){
            this.restUsersDataSource.refreshToken(new ITokenCallback() {
                @Override
                public void onSuccess(Token token) {
                    localUsersDataSource.setToken(token);
                    if(localUsersDataSource.getDeviceId().isPresent()) {
                        restUsersDataSource.deleteRegistrationId(
                                token.getIdToken(),
                                localUsersDataSource.getLoggedUser().getUid(),
                                localUsersDataSource.getDeviceId().get());
                    }
                    localUsersDataSource.logout();
                    restUsersDataSource.logout(callback);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        } else if (localUsersDataSource.getDeviceId().isPresent()) {
            restUsersDataSource.deleteRegistrationId(
                    this.localUsersDataSource.getToken().get().getIdToken(),
                    localUsersDataSource.getLoggedUser().getUid(),
                    localUsersDataSource.getDeviceId().get());
        } else {
            this.localUsersDataSource.logout();
            this.restUsersDataSource.logout(callback);
        }
    }
}
