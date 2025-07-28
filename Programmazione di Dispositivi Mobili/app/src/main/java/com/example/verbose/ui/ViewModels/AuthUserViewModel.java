package com.example.verbose.ui.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.verbose.model.AuthUser;
import com.example.verbose.model.Result;
import com.example.verbose.repository.UsersRepository;
import com.example.verbose.sources.IUsernameCallback;
import com.example.verbose.sources.IUsersCallback;
import com.example.verbose.util.ServiceLocator;

//TODO remove caller names before release

public class AuthUserViewModel extends ViewModel {
    private final MutableLiveData<Result<AuthUser>> userMutableLiveData;
    private final MutableLiveData<Result<Boolean>> freeUsernameMutableLiveData;

    private final UsersRepository usersRepository;

    //private final IUsersCallback callback;

    public AuthUserViewModel(){
        userMutableLiveData = new MutableLiveData<>();
        freeUsernameMutableLiveData = new MutableLiveData<>();

        usersRepository = ServiceLocator.getInstance().getUsersRepository();
        /*callback = new IUsersCallback() {
            @Override
            public void onSuccess(User user) {
                userMutableLiveData.postValue(new Result<>(user));
            }

            @Override
            public void onFailure(Throwable throwable) {
                userMutableLiveData.postValue(new Result<>(throwable));
            }
        };*/
    }

    public LiveData<Result<AuthUser>> loadUserData() {
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

    public LiveData<Result<AuthUser>> signIn(String idToken){
        Result.EventType type = Result.EventType.SIGN_IN;
        this.usersRepository.signIn(idToken, new IUsersCallback() {
            @Override
            public void onSuccess(AuthUser authUser) {
                userMutableLiveData.postValue(new Result<>(authUser,type, "signIn"));
            }

            @Override
            public void onFailure(Throwable throwable) {
                userMutableLiveData.postValue(new Result<>(throwable, type, "signIn"));
            }
        });
        return userMutableLiveData;
    }

    public LiveData<Result<AuthUser>> signIn(String email, String password){
        Result.EventType type = Result.EventType.SIGN_IN;
        this.usersRepository.signIn(email, password, new IUsersCallback() {
            @Override
            public void onSuccess(AuthUser authUser) {
                userMutableLiveData.postValue(new Result<>(authUser, type, "signIn"));
            }

            @Override
            public void onFailure(Throwable throwable) {
                userMutableLiveData.postValue(new Result<>(throwable, type, "signIn"));
            }
        });
        return userMutableLiveData;
    }

    public LiveData<Result<AuthUser>> signUp(String idToken){
        Result.EventType type = Result.EventType.SIGN_UP;
        this.usersRepository.signUp(idToken, new IUsersCallback() {
            @Override
            public void onSuccess(AuthUser authUser) {
                userMutableLiveData.postValue(new Result<>(authUser, type, "signUp"));
            }

            @Override
            public void onFailure(Throwable throwable) {
                userMutableLiveData.postValue(new Result<>(throwable, type, "signUp"));
            }
        });
        return userMutableLiveData;
    }

    public LiveData<Result<AuthUser>> signUp(String email, String password){
        Result.EventType type = Result.EventType.SIGN_UP;
        this.usersRepository.signUp(email, password, new IUsersCallback() {
            @Override
            public void onSuccess(AuthUser authUser) {
                userMutableLiveData.postValue(new Result<>(authUser, type, "signUp"));
            }

            @Override
            public void onFailure(Throwable throwable) {
                userMutableLiveData.postValue(new Result<>(throwable, type, "signUp"));
            }
        });
        return userMutableLiveData;
    }

    public LiveData<Result<AuthUser>> updateData(AuthUser updatedAuthUser){
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

    public LiveData<Result<Boolean>> checkUsername(String username){
        Result.EventType type = Result.EventType.CHECK_USERNAME;
        this.usersRepository.isUsernameFree(username, new IUsernameCallback() {
            @Override
            public void onSuccess(Boolean result) {
                freeUsernameMutableLiveData.postValue(new Result<>(result, type, "checkUsername"));
            }

            @Override
            public void onFailure(Throwable throwable) {
                freeUsernameMutableLiveData.postValue(new Result<>(throwable, type, "checkUsername"));
            }
        });

        return freeUsernameMutableLiveData;
    }

    public LiveData<Result<AuthUser>> getUserLiveData() {
        return userMutableLiveData;
    }

    public LiveData<Result<Boolean>> getFreeUserNameLiveData() {
        return freeUsernameMutableLiveData;
    }
}
