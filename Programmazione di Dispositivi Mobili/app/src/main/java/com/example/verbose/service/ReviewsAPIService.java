package com.example.verbose.service;

import com.example.verbose.model.Review;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ReviewsAPIService {
    @GET("reviews/{id}")
    Call<Review> getReview(@Path("id") String id);

    @POST("reviews/")
    Call<Review> createReview(@Header("Authorization") String idToken, @Body Review review);

    @PUT("reviews/{id}")
    Call<Review> updateReview(@Header("Authorization") String idToken, @Path("id") String id, @Body Review review);

    @DELETE("reviews/{id}/delete")
    Call<Void> deleteReview(@Header("Authorization") String idToken, @Path("id") String id);
}
