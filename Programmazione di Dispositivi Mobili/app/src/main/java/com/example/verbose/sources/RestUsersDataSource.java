package com.example.verbose.sources;

import android.util.Log;

import com.example.verbose.model.Appointment;
import com.example.verbose.model.Repetition;
import com.example.verbose.model.Review;
import com.example.verbose.model.Token;
import com.example.verbose.model.AuthUser;
import com.example.verbose.service.UsersAPIService;
import com.example.verbose.util.ServiceLocator;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestUsersDataSource {
    private static final String TAG = RestUsersDataSource.class.getSimpleName();
    private UsersAPIService usersAPIService;

    public RestUsersDataSource(){
        this.usersAPIService = ServiceLocator.getInstance().getUsersAPIService();
    }

    public void refreshToken(ITokenCallback callback){
        FirebaseAuth.getInstance().getCurrentUser().getIdToken(false)
                .addOnSuccessListener(getTokenResult -> {
                    callback.onSuccess(
                            new Token(getTokenResult.getToken(), getTokenResult.getExpirationTimestamp())
                    );
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void signInWithGoogle(String idToken, IUsersCallback callback){
        assert idToken != null;

        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(firebaseCredential)
                .addOnSuccessListener(authResult -> getUserData(authResult.getUser().getUid(), callback))
                .addOnFailureListener(callback::onFailure);
    }

    public void signInWithCredentials(String email, String password, IUsersCallback callback){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> getUserData(authResult.getUser().getUid(), callback))
                .addOnFailureListener(callback::onFailure);
    }

    public void getLoggedUserData(IUsersCallback callback) {
        getUserData(FirebaseAuth.getInstance().getCurrentUser().getUid(), callback);
    }

    public void getUserData(String id, IUsersCallback callback){
        this.usersAPIService.getUserData(id).enqueue(new Callback<AuthUser>() {
            @Override
            public void onResponse(Call<AuthUser> call, Response<AuthUser> response) {
                if(response.isSuccessful())
                    Log.d(TAG, response.body().getUsername());
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<AuthUser> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
                callback.onFailure(throwable);
            }
        });
    }

    public void createUser(String idToken, IUsersCallback callback){
        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(firebaseCredential)
                .addOnSuccessListener(authResult -> {
                    authResult.getUser().getIdToken(false)
                            .addOnSuccessListener(getTokenResult -> {
                                this.usersAPIService.createUser(getTokenResult.getToken(), authResult.getUser().getUid()).enqueue(new Callback<AuthUser>() {
                                    @Override
                                    public void onResponse(Call<AuthUser> call, Response<AuthUser> response) {
                                        Log.d(TAG, response.body().getUsername());
                                        callback.onSuccess(response.body());
                                    }

                                    @Override
                                    public void onFailure(Call<AuthUser> call, Throwable throwable) {
                                        callback.onFailure(throwable);
                                    }
                                });
                            })
                            .addOnFailureListener(callback::onFailure);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void createUser(String email, String password, IUsersCallback callback){

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    authResult.getUser().getIdToken(false)
                            .addOnSuccessListener(getTokenResult -> {
                                this.usersAPIService.createUser(getTokenResult.getToken(), authResult.getUser().getUid()).enqueue(new Callback<AuthUser>() {
                                    @Override
                                    public void onResponse(Call<AuthUser> call, Response<AuthUser> response) {
                                        Log.d(TAG, response.body().getUsername());
                                        callback.onSuccess(response.body());
                                    }

                                    @Override
                                    public void onFailure(Call<AuthUser> call, Throwable throwable) {
                                        callback.onFailure(throwable);
                                    }
                                });
                            })
                            .addOnFailureListener(callback::onFailure);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void updateRegistrationId(String idToken, String id, String deviceId,String registrationToken){
        this.usersAPIService.updateRegistrationId(idToken, id, deviceId,registrationToken).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //TODO
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                //TODO
            }
        });
    }

    public void deleteRegistrationId(String idToken, String id, String deviceId){
        this.usersAPIService.deleteRegistrationId(idToken, id, deviceId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //TODO
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                //TODO
            }
        });
    }

    public void updateUserData(String idToken, AuthUser authUser, IUsersCallback callback){
        this.usersAPIService.updateUserData(idToken, authUser.getUid(), authUser).enqueue(new Callback<AuthUser>() {
            @Override
            public void onResponse(Call<AuthUser> call, Response<AuthUser> response) {
                Log.d(TAG, response.body().getUsername());
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<AuthUser> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
                callback.onFailure(throwable);
            }
        });
    }

    public void updateProfilePic(String idToken, String id, File picture, IUsersCallback callback){
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), picture);
        MultipartBody.Part body = MultipartBody.Part.createFormData("profile_pic", picture.getName(), requestFile);
        this.usersAPIService.updateProfilePic(idToken, id, body).enqueue(new Callback<AuthUser>() {
            @Override
            public void onResponse(Call<AuthUser> call, Response<AuthUser> response) {
                Log.d(TAG, response.body().getUid());
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<AuthUser> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
                callback.onFailure(throwable);
            }
        });
    }

    public void getUserRepetitions(String idToken, String id, IRepetitionsCallback callback){
        this.usersAPIService.getUserRepetitions(idToken, id).enqueue(new Callback<List<Repetition>>() {
            @Override
            public void onResponse(Call<List<Repetition>> call, Response<List<Repetition>> response) {
                Log.d(TAG, response.body().toString());
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Repetition>> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
                callback.onFailure(throwable);
            }
        });
    }

    public void getUserPendingRequests(String idToken, String id, IAppointmentsCallback callback){
        this.usersAPIService.getUserPendingRequests(idToken, id).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                response.body().forEach(request -> Log.d(TAG, request.getId()));
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
                callback.onFailure(throwable);
            }
        });
    }

    public void getUserAcceptedRequests(String idToken, String id, IAppointmentsCallback callback){
        this.usersAPIService.getUserAcceptedRequests(idToken, id).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                response.body().forEach(request -> Log.d(TAG, request.getId()));
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
                callback.onFailure(throwable);
            }
        });
    }

    public void getUserClosedRequests(String idToken, String id, IAppointmentsCallback callback) {
        this.usersAPIService.getUserClosedRequests(idToken, id).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                Log.d(TAG, response.body().toString());
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
                callback.onFailure(throwable);
            }
        });
    }

    public void getUserOpenAppointments(String idToken, String id, IAppointmentsCallback callback){
        this.usersAPIService.getUserOpenAppointments(idToken, id).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                Log.d(TAG, response.body().toString());
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
                callback.onFailure(throwable);
            }
        });
    }

    public void getUserNotifications(String idToken, String id, IAppointmentsCallback callback){
        this.usersAPIService.getUserIncoming(idToken, id).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                Log.d(TAG, response.body().toString());
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
                callback.onFailure(throwable);
            }
        });
    }

    public void getUserFavorites(String idToken, String id, IRepetitionsCallback callback) {
        this.usersAPIService.getUserFavorites(idToken, id).enqueue(new Callback<List<Repetition>>() {
            @Override
            public void onResponse(Call<List<Repetition>> call, Response<List<Repetition>> response) {
                Log.d(TAG, response.body().toString());
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Repetition>> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
                callback.onFailure(throwable);
            }
        });
    }

    public void getUserReviews(String id, IReviewsCallback callback){
        this.usersAPIService.getUserReviews(id).enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
                callback.onFailure(throwable);
            }
        });
    }

    public void isUsernameFree(String username, IUsernameCallback callback){
        this.usersAPIService.isUsernameFree(username).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    public void logout(IUsersCallback callback){
        FirebaseAuth.getInstance().signOut();
        callback.onSuccess(null);
    }
}
