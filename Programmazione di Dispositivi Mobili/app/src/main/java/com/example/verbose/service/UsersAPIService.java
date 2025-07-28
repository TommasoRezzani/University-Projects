package com.example.verbose.service;

import com.example.verbose.model.Appointment;
import com.example.verbose.model.Repetition;
import com.example.verbose.model.Review;
import com.example.verbose.model.AuthUser;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UsersAPIService {
    @GET("users/{id}")
    Call<AuthUser> getUserData(@Path("id") String id);

    @PUT("users/{id}")
    Call<AuthUser> createUser(@Header("Authorization") String idToken, @Path("id") String id);

    @FormUrlEncoded
    @POST("users/{id}/device")
    Call<Void> updateRegistrationId(@Header("Authorization") String idToken, @Path("id") String id, @Field("device_id") String deviceId,@Field("registration_id") String registrationToken);

    @FormUrlEncoded
    @PUT("users/{id}/delete_device")
    Call<Void> deleteRegistrationId(@Header("Authorization") String idToken, @Path("id") String id, @Field("device_id") String deviceId);

    @PUT("users/{id}")
    Call<AuthUser> updateUserData(@Header("Authorization") String idToken, @Path("id") String id, @Body AuthUser authUser);

    @Multipart
    @POST("users/{id}/update_picture")
    Call<AuthUser> updateProfilePic(@Header("Authorization") String idToken, @Path("id") String id, @Part MultipartBody.Part image);

    @GET("users/{id}/repetitions")
    Call<List<Repetition>> getUserRepetitions(@Header("Authorization") String idToken, @Path("id") String id);

    @GET("users/{id}/pending")
    Call<List<Appointment>> getUserPendingRequests(@Header("Authorization") String idToken, @Path("id") String id);

    @GET("users/{id}/accepted")
    Call<List<Appointment>> getUserAcceptedRequests(@Header("Authorization") String idToken, @Path("id") String id);

    @GET("users/{id}/closed")
    Call<List<Appointment>> getUserClosedRequests(@Header("Authorization") String idToken, @Path("id") String id);

    @GET("users/{id}/open")
    Call<List<Appointment>> getUserOpenAppointments(@Header("Authorization") String idToken, @Path("id") String id);

    @GET("users/{id}/incoming")
    Call<List<Appointment>> getUserIncoming(@Header("Authorization") String idToken, @Path("id") String id);

    @GET("users/{id}/favorites")
    Call<List<Repetition>> getUserFavorites(@Header("Authorization") String idToken, @Path("id") String id);

    @GET("users/{id}/reviews")
    Call<List<Review>> getUserReviews(@Path("id") String id);

    @FormUrlEncoded
    @POST("free_username")
    Call<Boolean> isUsernameFree(@Field("username") String username);
}
