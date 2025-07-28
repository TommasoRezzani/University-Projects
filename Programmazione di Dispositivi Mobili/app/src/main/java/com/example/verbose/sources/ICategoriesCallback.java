package com.example.verbose.sources;

import android.util.Log;

import com.example.verbose.model.Category;

import java.util.List;
import java.util.Set;

public interface ICategoriesCallback {
    void onSuccess(Set<Category> categories);
    void onFailure(Throwable throwable);
}
