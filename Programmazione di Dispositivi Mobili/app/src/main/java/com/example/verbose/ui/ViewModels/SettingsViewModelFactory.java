package com.example.verbose.ui.ViewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SettingsViewModelFactory implements ViewModelProvider.Factory {
    private final boolean enabled;

    public SettingsViewModelFactory(boolean enabled) {
        this.enabled = enabled;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(SettingsViewModel.class)){
            return (T) new SettingsViewModel(enabled);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
