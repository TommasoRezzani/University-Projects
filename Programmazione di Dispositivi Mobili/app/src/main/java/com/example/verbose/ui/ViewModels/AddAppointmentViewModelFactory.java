package com.example.verbose.ui.ViewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.verbose.model.Repetition;

public class AddAppointmentViewModelFactory implements ViewModelProvider.Factory {
    private final Repetition repetition;

    public AddAppointmentViewModelFactory(Repetition repetition){
        this.repetition = repetition;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(AddAppointmentViewModel.class)){
            return (T) new AddAppointmentViewModel(repetition);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
