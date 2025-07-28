package com.example.verbose.ui.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.verbose.model.Repetition;
import com.example.verbose.model.Result;
import com.example.verbose.model.Review;
import com.example.verbose.repository.RepetitionsRepository;
import com.example.verbose.sources.IReviewsCallback;
import com.example.verbose.util.ServiceLocator;

import java.util.List;

public class ReviewListViewModel extends ViewModel {
    private final Repetition repetition;
    private final RepetitionsRepository repetitionsRepository;
    private final MutableLiveData<Result<List<Review>>> reviewsMutableLiveData;

    public ReviewListViewModel(Repetition repetition){
        this.repetition = repetition;

        this.repetitionsRepository = ServiceLocator.getInstance().getRepetitionsRepository();

        this.reviewsMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<Result<List<Review>>> getReviews(){
        Result.EventType type = Result.EventType.GET_REVIEWS;
        this.repetitionsRepository.getRepetitionReviews(repetition, new IReviewsCallback() {
            @Override
            public void onSuccess(List<Review> reviews) {
                reviewsMutableLiveData.postValue(new Result<>(reviews, type, "getReviews"));
            }

            @Override
            public void onFailure(Throwable throwable) {
                reviewsMutableLiveData.postValue(new Result<>(throwable, type, "getReviews"));
            }
        });

        return reviewsMutableLiveData;
    }

    public Repetition getRepetition() {
        return repetition;
    }

    public LiveData<Result<List<Review>>> getReviewsLiveData() {
        return reviewsMutableLiveData;
    }
}
