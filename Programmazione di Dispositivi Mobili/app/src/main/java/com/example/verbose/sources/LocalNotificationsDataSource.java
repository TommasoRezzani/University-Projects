package com.example.verbose.sources;

import static androidx.sqlite.db.SupportSQLiteCompat.Api16Impl.deleteDatabase;

import android.content.Context;

import com.example.verbose.database.NotificationsDAO;
import com.example.verbose.database.VerboseRoomDatabase;
import com.example.verbose.model.Notification;

public class LocalNotificationsDataSource {
    private final NotificationsDAO notificationsDAO;

    public interface INotificationCountCallback{
        void onSuccess(int count);
        void onFailure(Throwable throwable);
    }

    public interface INewNotificationCallback {
        void onReceive(Notification notification);
    }

    public LocalNotificationsDataSource(Context context){
        notificationsDAO = VerboseRoomDatabase.getDatabase(context).notificationsDAO();
    }

    public void getNotifications(INotificationsCallback callback){
        VerboseRoomDatabase.databaseWriteExecutor.execute(() -> callback.onSuccess(notificationsDAO.getAll()));
    }

    public void getNotificationsCount(INotificationCountCallback callback)
    {
        VerboseRoomDatabase.databaseWriteExecutor.execute(() -> callback.onSuccess(notificationsDAO.notificationsCount()));
    }

    public void insertNotification(Notification notification){
        VerboseRoomDatabase.databaseWriteExecutor.execute(() -> {
            notificationsDAO.insert(notification);
        });
    }

    public void deleteNotification(Notification notification){
        VerboseRoomDatabase.databaseWriteExecutor.execute(() -> notificationsDAO.delete(notification));
    }

    public void deleteRequestNotification(String targetObjectId){
        VerboseRoomDatabase.databaseWriteExecutor.execute(() -> notificationsDAO.deleteRequestNotification(targetObjectId));
    }

    public void deleteAll() {
        VerboseRoomDatabase.databaseWriteExecutor.execute(notificationsDAO::deleteAll);
    }
}
