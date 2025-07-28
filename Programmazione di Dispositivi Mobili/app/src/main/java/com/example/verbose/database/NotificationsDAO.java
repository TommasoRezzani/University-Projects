package com.example.verbose.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.verbose.model.Notification;

import java.util.List;

@Dao
public interface NotificationsDAO {
    @Query("SELECT * FROM notification")
    List<Notification> getAll();

    @Query("SELECT COUNT(*) FROM notification")
    int notificationsCount();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Notification notification);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Notification> notifications);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Notification... notifications);

    @Delete
    void delete(Notification notification);

    @Query("DELETE FROM notification WHERE targetObjectId=:targetObjectId AND type = \"APPOINTMENT_REQUEST\"")
    void deleteRequestNotification(String targetObjectId);

    @Query("DELETE FROM notification")
    void deleteAll();
}
