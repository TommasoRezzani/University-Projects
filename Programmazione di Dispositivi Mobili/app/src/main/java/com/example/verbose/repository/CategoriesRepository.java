package com.example.verbose.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.verbose.Constants;
import com.example.verbose.model.Category;
import com.example.verbose.sources.ICategoriesCallback;
import com.example.verbose.sources.LocalCategoriesDataSource;
import com.example.verbose.sources.RestCategoriesDataSource;

import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class CategoriesRepository {
    private final LocalCategoriesDataSource localCategoriesDataSource;
    private final RestCategoriesDataSource restCategoriesDataSource;
    private final SharedPreferences sharedPreferences;
    private static AtomicLong lastUpdate;

    public CategoriesRepository(Context context){
        sharedPreferences = context.getSharedPreferences(Constants.LOCAL_STORAGE, Context.MODE_PRIVATE);
        if(lastUpdate == null){
            if(sharedPreferences.contains(Constants.LAST_CATEGORIES_UPDATE)){
                lastUpdate = new AtomicLong(sharedPreferences.getLong(Constants.LAST_CATEGORIES_UPDATE, 0));
            } else {
                lastUpdate = new AtomicLong(0);
            }
        }
        localCategoriesDataSource = new LocalCategoriesDataSource(context);
        restCategoriesDataSource = new RestCategoriesDataSource();
    }

    public void getCategories(ICategoriesCallback callback){
        long currentTime = System.currentTimeMillis();

        if(currentTime - CategoriesRepository.lastUpdate.get() > Constants.CATEGORIES_REFRESH_TIME){
            restCategoriesDataSource.getCategories(new ICategoriesCallback() {
                @Override
                public void onSuccess(Set<Category> categories) {
                    localCategoriesDataSource.insertCategories(categories, new ICategoriesCallback() {
                        @Override
                        public void onSuccess(Set<Category> categories) {
                            lastUpdate.set(currentTime);
                            sharedPreferences.edit().putLong(Constants.LAST_CATEGORIES_UPDATE, currentTime).apply();
                            callback.onSuccess(categories);
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            callback.onFailure(throwable);
                        }
                    });
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        }else{
            localCategoriesDataSource.getCategories(new ICategoriesCallback() {
                @Override
                public void onSuccess(Set<Category> categories) {
                    callback.onSuccess(categories);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        }
    }


}
