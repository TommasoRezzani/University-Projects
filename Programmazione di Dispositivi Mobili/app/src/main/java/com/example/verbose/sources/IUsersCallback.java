package com.example.verbose.sources;

import com.example.verbose.model.AuthUser;

//TODO AuthUser -> UserInfo???

public interface IUsersCallback {
    void onSuccess(AuthUser authUser);
    void onFailure(Throwable throwable);
}
