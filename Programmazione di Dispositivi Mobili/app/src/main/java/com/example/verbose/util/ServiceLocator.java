package com.example.verbose.util;

import android.content.Context;

import com.example.verbose.Constants;
import com.example.verbose.repository.CategoriesRepository;
import com.example.verbose.repository.NotificationsRepository;
import com.example.verbose.repository.RepetitionsRepository;
import com.example.verbose.repository.UsersRepository;
import com.example.verbose.service.AppointmentsAPIService;
import com.example.verbose.service.CategoriesAPIService;
import com.example.verbose.service.RepetitionsAPIService;
import com.example.verbose.service.ReviewsAPIService;
import com.example.verbose.service.UsersAPIService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceLocator {
    private static volatile ServiceLocator INSTANCE = null;
    private final Retrofit retrofit;

    private ServiceLocator() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.VERBOSE_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            synchronized(ServiceLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceLocator();
                }
            }
        }
        return INSTANCE;
    }

    public CategoriesAPIService getCategoriesAPIService(){
        return retrofit.create(CategoriesAPIService.class);
    }

    public UsersAPIService getUsersAPIService(){
        return retrofit.create(UsersAPIService.class);
    }

    public RepetitionsAPIService getRepetitionsAPIService(){
        return retrofit.create(RepetitionsAPIService.class);
    }

    public AppointmentsAPIService getAppointmentsAPIService(){
        return retrofit.create(AppointmentsAPIService.class);
    }

    public ReviewsAPIService getReviewsAPIService(){
        return retrofit.create(ReviewsAPIService.class);
    }

    public CategoriesRepository getCategoriesRepository(Context context){
        return new CategoriesRepository(context);
    }

    public UsersRepository getUsersRepository(){
        return new UsersRepository();
    }

    public RepetitionsRepository getRepetitionsRepository(){
        return new RepetitionsRepository();
    }

    public NotificationsRepository getNotificationRepository(Context context){
        return new NotificationsRepository(context);
    }
}
