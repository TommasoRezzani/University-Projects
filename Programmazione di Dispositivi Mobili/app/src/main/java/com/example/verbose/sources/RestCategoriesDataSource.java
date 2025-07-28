package com.example.verbose.sources;

import android.util.Log;

import com.example.verbose.model.Category;
import com.example.verbose.service.CategoriesAPIService;
import com.example.verbose.util.ServiceLocator;

import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestCategoriesDataSource {
    private static final String TAG = RestCategoriesDataSource.class.getSimpleName();
    private CategoriesAPIService categoriesAPIService;

    public RestCategoriesDataSource(){
        this.categoriesAPIService = ServiceLocator.getInstance().getCategoriesAPIService();
    }

    public void getCategories(ICategoriesCallback callback){
        this.categoriesAPIService.getCategories().enqueue(new Callback<Set<Category>>() {
            @Override
            public void onResponse(Call<Set<Category>> call, Response<Set<Category>> response) {
                Log.d(TAG, response.toString());
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Set<Category>> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
                callback.onFailure(throwable);
            }
        });
    }
}
