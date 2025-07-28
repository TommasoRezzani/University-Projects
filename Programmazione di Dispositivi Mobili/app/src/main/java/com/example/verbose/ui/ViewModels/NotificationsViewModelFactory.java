package com.example.verbose.ui.ViewModels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class NotificationsViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public NotificationsViewModelFactory(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(NotificationsViewModel.class)){
            return (T) new NotificationsViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
