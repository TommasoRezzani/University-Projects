package com.example.verbose.sources;

import com.example.verbose.model.Review;

import java.util.List;

public interface IReviewsCallback {
    void onSuccess(List<Review> reviews);
    void onFailure(Throwable throwable);
}
