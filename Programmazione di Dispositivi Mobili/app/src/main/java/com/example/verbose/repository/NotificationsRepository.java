package com.example.verbose.repository;

import android.content.Context;
import android.util.Log;

import com.example.verbose.model.Notification;
import com.example.verbose.sources.INotificationsCallback;
import com.example.verbose.sources.LocalNotificationsDataSource;

import java.util.List;

public class NotificationsRepository {
    private static final String TAG = NotificationsRepository.class.getSimpleName();
    private final LocalNotificationsDataSource localNotificationsDataSource;

    public NotificationsRepository(Context context){
        localNotificationsDataSource = new LocalNotificationsDataSource(context);
    }

    public void getNotifications(INotificationsCallback callback){
        this.localNotificationsDataSource.getNotifications(new INotificationsCallback() {
            @Override
            public void onSuccess(List<Notification> notifications) {
                notifications.forEach(notification -> Log.i(TAG, notification.getTitle()));
                callback.onSuccess(notifications);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e(TAG, throwable.toString());
                callback.onFailure(throwable);
            }
        });
    }

    public void getNotificationsCount(LocalNotificationsDataSource.INotificationCountCallback callback){
        this.localNotificationsDataSource.getNotificationsCount(new LocalNotificationsDataSource.INotificationCountCallback() {
            @Override
            public void onSuccess(int count) {
                Log.d(TAG, String.valueOf(count));
                callback.onSuccess(count);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e(TAG, throwable.toString());
                callback.onFailure(throwable);
            }
        });
    }

    public void insertNotification(Notification notification){
        this.localNotificationsDataSource.insertNotification(notification);
    }

    public void deleteNotification(Notification notification){
        this.localNotificationsDataSource.deleteNotification(notification);
    }

    public void deleteRequestNotification(String targetObjectId){
        this.localNotificationsDataSource.deleteRequestNotification(targetObjectId);
    }

    public void deleteAll() {
        this.localNotificationsDataSource.deleteAll();
    }
}
