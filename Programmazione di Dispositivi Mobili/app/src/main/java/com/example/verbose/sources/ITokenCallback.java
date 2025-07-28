package com.example.verbose.sources;

import com.example.verbose.model.Token;

public interface ITokenCallback {
    void onSuccess(Token token);
    void onFailure(Throwable throwable);
}
