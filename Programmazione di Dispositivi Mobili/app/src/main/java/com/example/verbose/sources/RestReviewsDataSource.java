package com.example.verbose.sources;

import android.util.Log;

import com.example.verbose.model.Review;
import com.example.verbose.service.ReviewsAPIService;
import com.example.verbose.util.ServiceLocator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestReviewsDataSource {
    private static final String TAG = RestReviewsDataSource.class.getSimpleName();

    private ReviewsAPIService reviewsAPIService;

    public RestReviewsDataSource(){
        this.reviewsAPIService = ServiceLocator.getInstance().getReviewsAPIService();
    }

    public void getReview(String id){
        this.reviewsAPIService.getReview(id).enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                Log.d(TAG, response.body().getContent());
            }

            @Override
            public void onFailure(Call<Review> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
            }
        });
    }

    public void createReview(String idToken, Review review, IReviewsCallback callback){
        assert review.getId() == null;

        this.reviewsAPIService.createReview(idToken, review).enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                ArrayList<Review> result = new ArrayList<>();
                result.add(response.body());
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(Call<Review> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
            }
        });
    }

    public void updateReview(String idToken, Review review){
        assert review.getId() != null;

        this.reviewsAPIService.updateReview(idToken, review.getId(), review).enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                Log.d(TAG, response.body().getContent());
            }

            @Override
            public void onFailure(Call<Review> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
            }
        });
    }

    public void deleteReview(String idToken, Review review){
        assert review.getId() != null;

        this.reviewsAPIService.deleteReview(idToken, review.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
            }
        });
    }
}
