package com.example.verbose.ui.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.verbose.model.Appointment;
import com.example.verbose.model.Notification;
import com.example.verbose.model.Result;
import com.example.verbose.repository.NotificationsRepository;
import com.example.verbose.repository.UsersRepository;
import com.example.verbose.sources.IAppointmentsCallback;
import com.example.verbose.sources.INotificationsCallback;
import com.example.verbose.util.ServiceLocator;

import java.util.List;

public class NotificationsViewModel extends ViewModel {
    private static final String TAG = NotificationsViewModel.class.getSimpleName();

    private final NotificationsRepository notificationsRepository;
    private final UsersRepository usersRepository;

    private final MutableLiveData<Result<List<Notification>>> notificationsMutableLiveData;

    public NotificationsViewModel(Context context){
        notificationsRepository = ServiceLocator.getInstance().getNotificationRepository(context);
        usersRepository = ServiceLocator.getInstance().getUsersRepository();

        notificationsMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<Result<List<Notification>>> getNotifications(){
        Result.EventType type = Result.EventType.GET_NOTIFICATIONS;

        this.notificationsRepository.getNotifications(new INotificationsCallback() {
            @Override
            public void onSuccess(List<Notification> notifications) {
                notificationsMutableLiveData.postValue(new Result<>(notifications, type, "getNotifications"));
            }

            @Override
            public void onFailure(Throwable throwable) {
                notificationsMutableLiveData.postValue(new Result<>(throwable, type, "getNotifications"));
            }
        });

        return notificationsMutableLiveData;
    }

    public void answerRequest(String targetObjectId, boolean accepted){
        if(accepted) {
            this.usersRepository.acceptUserRequest(targetObjectId, new IAppointmentsCallback() {
                @Override
                public void onSuccess(List<Appointment> appointments) {

                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        } else {
            this.usersRepository.refuseUserRequest(targetObjectId, new IAppointmentsCallback() {
                @Override
                public void onSuccess(List<Appointment> appointments) {

                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        }
    }

    public LiveData<Result<List<Notification>>> getNotificationsLiveData() {
        return notificationsMutableLiveData;
    }

    public void deleteNotification(Notification notification) {
        this.notificationsRepository.deleteNotification(notification);
    }
}
