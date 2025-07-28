package com.example.verbose.repository;

import android.content.Context;

import com.example.verbose.model.Appointment;
import com.example.verbose.model.Repetition;
import com.example.verbose.model.RepetitionInfo;
import com.example.verbose.model.Review;
import com.example.verbose.model.Token;
import com.example.verbose.sources.IAppointmentsCallback;
import com.example.verbose.sources.IRepetitionsCallback;
import com.example.verbose.sources.IReviewsCallback;
import com.example.verbose.sources.ISlotsCallback;
import com.example.verbose.sources.ITokenCallback;
import com.example.verbose.sources.LocalUsersDataSource;
import com.example.verbose.sources.RestAppointmentsDataSource;
import com.example.verbose.sources.RestRepetitionsDataSource;
import com.example.verbose.sources.RestReviewsDataSource;
import com.example.verbose.sources.RestUsersDataSource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class RepetitionsRepository {
    private final RestRepetitionsDataSource restRepetitionsDataSource;
    private final RestReviewsDataSource restReviewsDataSource;
    private final RestUsersDataSource restUsersDataSource;
    private final LocalUsersDataSource localUsersDataSource;
    private final RestAppointmentsDataSource restAppointmentsDataSource;

    public RepetitionsRepository(){
        this.restRepetitionsDataSource = new RestRepetitionsDataSource();
        this.restReviewsDataSource = new RestReviewsDataSource();
        this.restUsersDataSource = new RestUsersDataSource();
        this.localUsersDataSource = new LocalUsersDataSource();
        this.restAppointmentsDataSource = new RestAppointmentsDataSource();
    }

    public void searchRepetitions(String query, IRepetitionsCallback callback){
        this.restRepetitionsDataSource.searchRepetitions(query, callback);
    }

    public void getRepetition(String id, IRepetitionsCallback callback){
        this.restRepetitionsDataSource.getRepetition(id, callback);
    }

    public void getRepetitionReviews(Repetition repetition, IReviewsCallback callback){
        this.restRepetitionsDataSource.getRepetitionReviews(repetition, callback);
    }

    public void addRepetitionReview(Review review, IReviewsCallback callback){
        assert this.localUsersDataSource.hasUser();

        if(!this.localUsersDataSource.getToken().isPresent() || this.localUsersDataSource.getToken().get().isExpired()){
            this.restUsersDataSource.refreshToken(new ITokenCallback() {
                @Override
                public void onSuccess(Token token) {
                    localUsersDataSource.setToken(token);
                    restReviewsDataSource.createReview(
                            token.getIdToken(),
                            review,
                            callback);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        }else{
            restReviewsDataSource.createReview(
                    this.localUsersDataSource.getToken().get().getIdToken(),
                    review,
                    callback);
        }
    }

    public void getRepetitionSlotsByDate(Repetition repetition, Date date, ISlotsCallback callback){
        this.restRepetitionsDataSource.getRepetitionSlotsByDate(repetition, date, callback);
    }
}
