package com.example.verbose.ui.ViewModels;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.verbose.Constants;
import com.example.verbose.model.AuthUser;
import com.example.verbose.model.Repetition;
import com.example.verbose.model.Result;
import com.example.verbose.repository.NotificationsRepository;
import com.example.verbose.repository.UsersRepository;
import com.example.verbose.sources.IRepetitionsCallback;
import com.example.verbose.sources.IUsersCallback;
import com.example.verbose.util.ServiceLocator;

import java.io.File;
import java.util.List;

public class ProfileViewModel extends ViewModel {
    private final UsersRepository usersRepository;
    private final NotificationsRepository notificationsRepository;

    private final SharedPreferences sharedPreferences;

    private final MutableLiveData<Result<AuthUser>> userMutableLiveData;
    private final MutableLiveData<Result<List<Repetition>>> repetitionsMutableLiveData;

    public ProfileViewModel(Context context){
        this.usersRepository = ServiceLocator.getInstance().getUsersRepository();
        this.notificationsRepository = ServiceLocator.getInstance().getNotificationRepository(context);

        this.sharedPreferences = context.getSharedPreferences(Constants.LOCAL_STORAGE, Context.MODE_PRIVATE);

        this.userMutableLiveData = new MutableLiveData<>();
        this.repetitionsMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<Result<AuthUser>> getUserData(){
        Result.EventType type = Result.EventType.GET_USER;

        this.usersRepository.getLoggedUserData(new IUsersCallback() {
            @Override
            public void onSuccess(AuthUser authUser) {
                userMutableLiveData.postValue(new Result<>(authUser, type, "loadUserData"));
            }

            @Override
            public void onFailure(Throwable throwable) {
                userMutableLiveData.postValue(new Result<>(throwable, type, "loadUserData"));
            }
        });

        return userMutableLiveData;
    }

    public LiveData<Result<AuthUser>> updateProfile(AuthUser updatedAuthUser){
        Result.EventType type = Result.EventType.UPDATE_USER;
        this.usersRepository.updateUserData(updatedAuthUser, new IUsersCallback() {
            @Override
            public void onSuccess(AuthUser authUser) {
                userMutableLiveData.postValue(new Result<>(authUser, type, "updateData"));
            }

            @Override
            public void onFailure(Throwable throwable) {
                userMutableLiveData.postValue(new Result<>(throwable, type, "updateData"));
            }
        });
        return userMutableLiveData;
    }

    public LiveData<Result<AuthUser>> updateProfilePic(File picture){
        Result.EventType type = Result.EventType.UPDATE_USER;

        this.usersRepository.updateProfilePic(picture, new IUsersCallback() {
            @Override
            public void onSuccess(AuthUser authUser) {
                userMutableLiveData.postValue(new Result<>(authUser, type, "updateProfilePic"));
            }

            @Override
            public void onFailure(Throwable throwable) {
                userMutableLiveData.postValue(new Result<>(throwable, type, "updateProfilePic"));
            }
        });

        return userMutableLiveData;
    }

    public LiveData<Result<List<Repetition>>> getRepetitions(){
        Result.EventType type = Result.EventType.GET_REPETITIONS;
        this.usersRepository.getUserRepetitions(new IRepetitionsCallback() {
            @Override
            public void onSuccess(List<Repetition> repetitions) {
                repetitionsMutableLiveData.postValue(new Result<>(repetitions, type, "getRepetitions"));
            }

            @Override
            public void onFailure(Throwable throwable) {
                repetitionsMutableLiveData.postValue(new Result<>(throwable, type, "getRepetitions"));
            }
        });

        return repetitionsMutableLiveData;
    }

    public LiveData<Result<AuthUser>> logout(){
        Result.EventType type = Result.EventType.LOGOUT;

        this.notificationsRepository.deleteAll();
        sharedPreferences.edit().putBoolean("first_launch", true).apply();
        this.usersRepository.logout(new IUsersCallback() {
            @Override
            public void onSuccess(AuthUser authUser) {
                userMutableLiveData.postValue(new Result<>(null, type, "logout"));
            }

            @Override
            public void onFailure(Throwable throwable) {
                userMutableLiveData.postValue(new Result<>(throwable, type, "logout"));
            }
        });

        return userMutableLiveData;
    }

    public LiveData<Result<AuthUser>> getUserLiveData() {
        return userMutableLiveData;
    }

    public LiveData<Result<List<Repetition>>> getRepetitionsLiveData() {
        return repetitionsMutableLiveData;
    }

    public LiveData<Result<List<Repetition>>> closeRepetition(Repetition target){

        target.close();

        this.usersRepository.updateUserRepetition(target, new IRepetitionsCallback() {
            @Override
            public void onSuccess(List<Repetition> repetitions) {
                repetitionsMutableLiveData.postValue(repetitionsMutableLiveData.getValue());
            }
            @Override
            public void onFailure(Throwable throwable) {

            }
        });
        return repetitionsMutableLiveData;
    }
}
