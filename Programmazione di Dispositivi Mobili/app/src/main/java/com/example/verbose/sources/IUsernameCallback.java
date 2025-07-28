package com.example.verbose.sources;

public interface IUsernameCallback {
    void onSuccess(Boolean result);
    void onFailure(Throwable throwable);
}
