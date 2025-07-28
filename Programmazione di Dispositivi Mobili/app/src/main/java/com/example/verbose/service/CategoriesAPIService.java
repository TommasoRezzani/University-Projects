package com.example.verbose.service;

import com.example.verbose.model.Category;

import java.util.Set;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoriesAPIService {
    @GET("categories/")
    Call<Set<Category>> getCategories();
}
