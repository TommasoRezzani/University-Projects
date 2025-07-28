package com.example.verbose.ui.ViewModels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CategoriesViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public CategoriesViewModelFactory(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(CategoriesViewModel.class)){
            return (T) new CategoriesViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
