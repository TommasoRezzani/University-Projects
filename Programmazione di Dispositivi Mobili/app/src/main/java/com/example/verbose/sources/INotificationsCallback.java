package com.example.verbose.sources;

import com.example.verbose.model.Notification;

import java.util.List;

public interface INotificationsCallback {
    void onSuccess(List<Notification> notifications);
    void onFailure(Throwable throwable);
}
