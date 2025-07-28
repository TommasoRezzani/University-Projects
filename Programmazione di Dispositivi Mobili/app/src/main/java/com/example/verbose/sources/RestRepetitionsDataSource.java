package com.example.verbose.sources;

import static android.icu.text.DateFormat.getPatternInstance;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import com.example.verbose.model.Repetition;
import com.example.verbose.model.Review;
import com.example.verbose.service.RepetitionsAPIService;
import com.example.verbose.util.ServiceLocator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestRepetitionsDataSource {
    private static final String TAG = RestRepetitionsDataSource.class.getSimpleName();
    private RepetitionsAPIService repetitionsAPIService;

    public RestRepetitionsDataSource(){
        this.repetitionsAPIService = ServiceLocator.getInstance().getRepetitionsAPIService();
    }

    public void getRepetition(String id, IRepetitionsCallback callback){
        this.repetitionsAPIService.getRepetition(id).enqueue(new Callback<Repetition>() {
            @Override
            public void onResponse(Call<Repetition> call, Response<Repetition> response) {
                Log.d(TAG, response.body().toString());

                List<Repetition> repetitions = new ArrayList<>();
                repetitions.add(response.body());

                callback.onSuccess(repetitions);
            }

            @Override
            public void onFailure(Call<Repetition> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
                callback.onFailure(throwable);
            }
        });
    }

    public void createRepetition(String idToken, Repetition repetition, IRepetitionsCallback callback){
        assert repetition.getId() == null; //L'id verrà assegnato dal server alla creazione della ripetizione
        this.repetitionsAPIService.createRepetition(idToken, repetition).enqueue(new Callback<Repetition>() {
            @Override
            public void onResponse(Call<Repetition> call, Response<Repetition> response) {
                Log.d(TAG, response.body().toString());

                List<Repetition> repetitions = new ArrayList<>();
                repetitions.add(response.body());

                callback.onSuccess(repetitions);
            }

            @Override
            public void onFailure(Call<Repetition> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
                callback.onFailure(throwable);
            }
        });
    }

    public void updateRepetition(String idToken, Repetition repetition, IRepetitionsCallback callback){
        assert repetition.getId() != null; //La ripetizione deve già esistere sul server

        this.repetitionsAPIService.updateRepetition(idToken, repetition.getId(), repetition).enqueue(new Callback<Repetition>() {
            @Override
            public void onResponse(Call<Repetition> call, Response<Repetition> response) {
                Log.d(TAG, repetition.getId());

                List<Repetition> repetitions = new ArrayList<>();
                repetitions.add(response.body());

                callback.onSuccess(repetitions);
            }

            @Override
            public void onFailure(Call<Repetition> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
                callback.onFailure(throwable);
            }
        });
    }

    public void getRepetitionReviews(String id, IReviewsCallback callback){
        this.repetitionsAPIService.getRepetitionReviews(id).enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                response.body().forEach(review -> {
                    Log.d(TAG, review.getContent());
                });

                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
                callback.onFailure(throwable);
            }
        });
    }

    public void getRepetitionReviews(Repetition repetition, IReviewsCallback callback){
        assert repetition.getId() != null;

        getRepetitionReviews(repetition.getId(), callback);
    }

    public void searchRepetitions(String query, IRepetitionsCallback callback){
        this.repetitionsAPIService.searchRepetitions(query).enqueue(new Callback<List<Repetition>>() {
            @Override
            public void onResponse(Call<List<Repetition>> call, Response<List<Repetition>> response) {
                response.body().forEach(repetition -> Log.d(TAG, repetition.getId()));
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Repetition>> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
                callback.onFailure(throwable);
            }
        });
    }

    public void getRepetitionSlotsByDate(Repetition repetition, Date date, ISlotsCallback callback){
        //Data formattata per conformarsi con il formato di data presente nel database
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = simpleDateFormat.format(date);
        this.repetitionsAPIService.getRepetitionSlots(repetition.getId(), formattedDate).enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                response.body().forEach(slot -> Log.d(TAG, slot.toString()));
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
                callback.onFailure(throwable);
            }
        });
    }
}
