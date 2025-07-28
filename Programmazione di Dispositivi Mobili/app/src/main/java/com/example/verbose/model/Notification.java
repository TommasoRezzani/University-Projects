package com.example.verbose.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity
public class Notification {
    private static final String TAG = Notification.class.getSimpleName();

    @PrimaryKey(autoGenerate = true)
    private int id;
    private final long time;
    private final String title;
    private final String content;

    public enum NotificationType{
        APPOINTMENT_REQUEST(0),
        APPOINTMENT_REQUEST_DELETED(1),
        INCOMING_APPOINTMENT(2),
        APPOINTMENT_DELETED(3),
        APPOINTMENT_CONFIRMED(4),
        APPOINTMENT_REFUSED(5),
        APPOINTMENT_CLOSED(6);

        private final int value;

        NotificationType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    private final NotificationType type;
    private final String targetObjectId;

    public Notification(long time, String title, String content, NotificationType type, String targetObjectId) {
        this.time = time;
        this.title = title;
        this.content = content;
        this.type = type;
        this.targetObjectId = targetObjectId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public Date getDateTime(){
        return new Date(time);
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public NotificationType getType() {
        return type;
    }

    public String getTargetObjectId() {
        return targetObjectId;
    }
}
