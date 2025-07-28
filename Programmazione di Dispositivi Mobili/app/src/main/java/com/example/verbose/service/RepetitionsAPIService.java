package com.example.verbose.service;

import com.example.verbose.model.Repetition;
import com.example.verbose.model.Review;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RepetitionsAPIService {
    @GET("repetitions/{id}")
    Call<Repetition> getRepetition(@Path("id") String id);

    @POST("repetitions/")
    Call<Repetition> createRepetition(@Header("Authorization") String idToken, @Body Repetition repetition);

    @PUT("repetitions/{id}")
    Call<Repetition> updateRepetition(@Header("Authorization") String idToken, @Path("id") String id, @Body Repetition repetition);

    @GET("repetitions/{id}/reviews")
    Call<List<Review>> getRepetitionReviews(@Path("id") String id);

    @GET("repetitions/search")
    Call<List<Repetition>> searchRepetitions(@Query("query") String query);

    @GET("repetitions/{id}/slots")
    Call<List<Integer>> getRepetitionSlots(@Path("id") String id, @Query("date") String date);
}
