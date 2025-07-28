package com.example.verbose.backgroudService;

import static android.icu.text.DateFormat.getPatternInstance;
import static com.example.verbose.Constants.LOCAL_STORAGE;
import static com.example.verbose.Constants.NOTIFICATIONS_SETTINGS;
import static com.example.verbose.Constants.NOTIFICATION_CHANNEL_ID;

import android.app.NotificationManager;
import android.icu.text.DateFormat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.verbose.R;
import com.example.verbose.model.Notification;
import com.example.verbose.model.Notification.NotificationType;
import com.example.verbose.repository.NotificationsRepository;
import com.example.verbose.sources.LocalNotificationsDataSource;
import com.example.verbose.ui.mainApp.MainActivity;
import com.example.verbose.util.ServiceLocator;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.sql.Date;
import java.util.Map;
import java.util.Random;

public class NotificationService extends FirebaseMessagingService {
    private static final String TAG = NotificationService.class.getSimpleName();
    private NotificationsRepository notificationsRepository;
    private final DateFormat dateFormat;

    public NotificationService() {
        super();
        dateFormat = getPatternInstance("yyMMd");
    }

    @Override
    public void onNewToken(@NonNull String token) {
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {

        if(this.notificationsRepository == null)
            this.notificationsRepository = ServiceLocator.getInstance().getNotificationRepository(getApplicationContext());

        Map<String, String> dataMap = message.getData();

        long sentTime = message.getSentTime();
        String targetObjectId = dataMap.get("target_object_id");
        NotificationType type;


        android.app.Notification androidNotification;

        switch (dataMap.get("type")){
            case "appointment_request":
                type = NotificationType.APPOINTMENT_REQUEST;
                androidNotification = buildAppointmentRequestNotification(dataMap);
                break;
            case "appointment_request_deleted":
                removeNotification(targetObjectId);
                return;
            case "incoming_appointment":
                type = NotificationType.INCOMING_APPOINTMENT;
                androidNotification = buildIncomingAppointmentNotification(dataMap);
                break;
            case "appointment_deleted":
                type = NotificationType.APPOINTMENT_DELETED;
                androidNotification = buildAppointmentDeletedNotification(dataMap);
                break;
            case "appointment_confirmed":
                type = NotificationType.APPOINTMENT_CONFIRMED;
                androidNotification = buildAppointmentConfirmedNotification(dataMap);
                break;
            case "appointment_refused":
                type = NotificationType.APPOINTMENT_REFUSED;
                androidNotification = buildAppointmentRefusedNotification(dataMap);
                break;
            case "appointment_closed":
                type = NotificationType.APPOINTMENT_CLOSED;
                androidNotification = buildAppointmentClosedNotification(dataMap);
                break;
            default:
                throw new RuntimeException();
        }

        Notification notification = new Notification(
                sentTime,
                androidNotification.extras.getString("android.title"),
                androidNotification.extras.getString("android.text"),
                type,
                targetObjectId
        );
        notificationsRepository.insertNotification(notification);

        if(MainActivity.getNotificationsBadge() != null) {
            MainActivity.getNotificationsBadge().setVisible(true);
            MainActivity.getNotificationsBadge().setNumber(MainActivity.getNotificationsBadge().getNumber() + 1);
        }

        boolean showNotifications = getSharedPreferences(LOCAL_STORAGE, MODE_PRIVATE).getBoolean(NOTIFICATIONS_SETTINGS, true);
        if(showNotifications)
            getSystemService(NotificationManager.class).notify(new Random().nextInt(), androidNotification);
    }

    private void removeNotification(String targetObjectId) {
        notificationsRepository.deleteRequestNotification(targetObjectId);
    }

    private android.app.Notification buildAppointmentRequestNotification(Map<String, String> dataMap){
        return  new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.request_send_outline)
                .setColor(getColor(R.color.verbose_logo_background))
                .setColorized(true)
                .setContentTitle(getString(R.string.appointment_request_title, dataMap.get("repetition_name")))
                .setContentText(getString(
                        R.string.appointment_request_content,
                        dataMap.get("client_username"),
                        dateFormat.format(Date.valueOf(dataMap.get("date"))),
                        dataMap.get("time_slot"),
                        dataMap.get("request_text")))
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build();
    }

    private android.app.Notification buildIncomingAppointmentNotification(Map<String, String> dataMap){
        return  new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_access_alarm_24)
                .setColor(getColor(R.color.verbose_logo_background))
                .setColorized(true)
                .setContentTitle(getString(R.string.incoming_title, dataMap.get("repetition_name"), dataMap.get("time_slot")))
                .setContentText(getString(R.string.incoming_content, dataMap.get("owner_username")))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build();
    }

    private android.app.Notification buildAppointmentDeletedNotification(Map<String, String> dataMap){
        return  new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_delete_outline_24)
                .setColor(getColor(R.color.verbose_logo_background))
                .setColorized(true)
                .setContentTitle(getString(R.string.deleted_title, dataMap.get("repetition_name")))
                .setContentText(
                        getString(
                                R.string.deleted_content,
                                dataMap.get("client_username"),
                                dateFormat.format(Date.valueOf(dataMap.get("date"))),
                                dataMap.get("time_slot")
                ))
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build();
    }

    private android.app.Notification buildAppointmentConfirmedNotification(Map<String, String> dataMap){
        return  new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.accepted_request_icon)
                .setColor(getColor(R.color.verbose_logo_background))
                .setColorized(true)
                .setContentTitle(getString(R.string.confirmed_title, dataMap.get("repetition_name")))
                .setContentText(
                        getString(
                                R.string.confirmed_content,
                                dataMap.get("owner_username"),
                                dateFormat.format(Date.valueOf(dataMap.get("date"))),
                                dataMap.get("time_slot")))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build();
    }

    private android.app.Notification buildAppointmentRefusedNotification(Map<String, String> dataMap){
        return  new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.refused_request_icon)
                .setColor(getColor(R.color.verbose_logo_background))
                .setColorized(true)
                .setContentTitle(getString(R.string.refused_title, dataMap.get("repetition_name")))
                .setContentText(
                        getString(
                                R.string.refused_content,
                                dataMap.get("owner_username"),
                                dateFormat.format(Date.valueOf(dataMap.get("date"))),
                                dataMap.get("time_slot")))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build();
    }

    private android.app.Notification buildAppointmentClosedNotification(Map<String, String> dataMap) {
        return  new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.review_checkmark_svgrepo_com)
                .setColor(getColor(R.color.verbose_logo_background))
                .setColorized(true)
                .setContentTitle(getString(R.string.closed_title, dataMap.get("repetition_name")))
                .setContentText(getString(R.string.closed_content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build();
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();

        Log.i(TAG, "DELETED");
    }
}
