package com.example.verbose.ui.ViewModels;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {
    private final MutableLiveData<Pair<Integer, String>> selectedLanguageMutableLiveData;
    private final MutableLiveData<Boolean> notificationsEnableMutableLiveData;

    public SettingsViewModel(Boolean notificationsEnabled){
        this.selectedLanguageMutableLiveData = new MutableLiveData<>();
        this.notificationsEnableMutableLiveData = new MutableLiveData<>(notificationsEnabled);

    }

    public LiveData<Pair<Integer, String>> changeLanguage(Integer position,String language){
        this.selectedLanguageMutableLiveData.postValue(new Pair<>(position ,language));
        return this.selectedLanguageMutableLiveData;
    }

    public LiveData<Boolean> setNotificationsEnabled(boolean enabled){
        this.notificationsEnableMutableLiveData.postValue(enabled);
        return this.notificationsEnableMutableLiveData;
    }
    public LiveData<Boolean> getNotificationsEnabledLiveData() {
        return notificationsEnableMutableLiveData;
    }

    public LiveData<Pair<Integer, String>> getSelectedLanguageLiveData() {
        return selectedLanguageMutableLiveData;
    }
}
