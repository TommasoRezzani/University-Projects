package com.example.verbose.ui.ViewModels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.verbose.model.Repetition;

public class ProfileViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public ProfileViewModelFactory(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(ProfileViewModel.class)){
            return (T) new ProfileViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
