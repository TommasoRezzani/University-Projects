package com.example.verbose.ui.ViewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.verbose.model.Repetition;

public class ReviewListViewModelFactory implements ViewModelProvider.Factory {
    private final Repetition repetition;

    public ReviewListViewModelFactory(Repetition repetition) {
        this.repetition = repetition;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(ReviewListViewModel.class)){
            return (T) new ReviewListViewModel(repetition);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
