package com.example.verbose.ui.ViewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.verbose.model.Appointment;
import com.example.verbose.model.Result;
import com.example.verbose.model.Review;
import com.example.verbose.repository.RepetitionsRepository;
import com.example.verbose.repository.UsersRepository;
import com.example.verbose.sources.IAppointmentsCallback;
import com.example.verbose.sources.IReviewsCallback;
import com.example.verbose.util.ServiceLocator;

import java.util.List;

public class AppointmentListViewModel extends ViewModel {
    private static final String TAG = AppointmentListViewModel.class.getSimpleName();

    private final UsersRepository usersRepository;
    private final RepetitionsRepository repetitionsRepository;

    private final MutableLiveData<Result<List<Appointment>>> sentAppointmentsMutableLiveData;
    private final MutableLiveData<Result<List<Appointment>>> recvAppointmentsMutableLiveData;
    private final MutableLiveData<Result<List<Appointment>>> closedAppointmentsMutableLiveData;

    private final MutableLiveData<Integer> tabIndex;
    private final MutableLiveData<Boolean> connectionErrorMutableLiveData;

    public AppointmentListViewModel(){
        this.usersRepository = ServiceLocator.getInstance().getUsersRepository();
        this.repetitionsRepository = ServiceLocator.getInstance().getRepetitionsRepository();

        this.sentAppointmentsMutableLiveData = new MutableLiveData<>();
        this.recvAppointmentsMutableLiveData = new MutableLiveData<>();
        this.closedAppointmentsMutableLiveData = new MutableLiveData<>();

        tabIndex = new MutableLiveData<>(0);
        connectionErrorMutableLiveData = new MutableLiveData<>(false);
    }

    //Ottiene gli appuntamenti, ancora aperti, richiesti(inviati) dall'utente
    public LiveData<Result<List<Appointment>>> getSentAppointments(){
        Result.EventType type = Result.EventType.GET_APPOINTMENTS;
        this.usersRepository.getUserOpenAppointments(new IAppointmentsCallback() {
            @Override
            public void onSuccess(List<Appointment> appointments) {
                sentAppointmentsMutableLiveData.postValue(new Result<>(appointments, type, "getSentAppointments"));
            }

            @Override
            public void onFailure(Throwable throwable) {
                sentAppointmentsMutableLiveData.postValue(new Result<>(throwable, type, "getSentAppointments"));
            }
        });

        return sentAppointmentsMutableLiveData;
    }

    public LiveData<Result<List<Appointment>>> getRecvAppointments(){
        Result.EventType type = Result.EventType.GET_APPOINTMENTS;

        this.usersRepository.getUserAcceptedRequests(new IAppointmentsCallback() {
            @Override
            public void onSuccess(List<Appointment> appointments) {
                recvAppointmentsMutableLiveData.postValue(new Result<>(appointments, type, "getSentAppointments"));
            }

            @Override
            public void onFailure(Throwable throwable) {
                recvAppointmentsMutableLiveData.postValue(new Result<>(throwable, type, "getSentAppointments"));
            }
        });

        return recvAppointmentsMutableLiveData;
    }

    public LiveData<Result<List<Appointment>>> getClosedAppointments(){
        Result.EventType type = Result.EventType.GET_APPOINTMENTS;

        this.usersRepository.getUserClosedRequests(new IAppointmentsCallback() {
            @Override
            public void onSuccess(List<Appointment> appointments) {
                closedAppointmentsMutableLiveData.postValue(new Result<>(appointments, type, "getSentAppointments"));
            }

            @Override
            public void onFailure(Throwable throwable) {
                closedAppointmentsMutableLiveData.postValue(new Result<>(throwable, type, "getSentAppointments"));
            }
        });

        return closedAppointmentsMutableLiveData;
    }

    public void closeAppointment(Appointment target) {
        this.usersRepository.closeAppointment(target, new IAppointmentsCallback() {
            @Override
            public void onSuccess(List<Appointment> appointments) {
                recvAppointmentsMutableLiveData.getValue().getData().remove(target);
                recvAppointmentsMutableLiveData.postValue(recvAppointmentsMutableLiveData.getValue());
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(TAG, throwable.toString());
            }
        });
    }

    public LiveData<Result<List<Appointment>>> deleteAppointment(Appointment target) {
        this.usersRepository.deleteUserAppointment(target);
        this.sentAppointmentsMutableLiveData.getValue().getData().remove(target);
        this.sentAppointmentsMutableLiveData.postValue(this.sentAppointmentsMutableLiveData.getValue());

        return this.sentAppointmentsMutableLiveData;
    }

    public LiveData<Result<Review>> sendReview(Appointment target, String reviewText, float vote) {
        Result.EventType type = Result.EventType.POST_REVIEW;

        Review review = new Review(target, reviewText, vote);

        this.repetitionsRepository.addRepetitionReview(review, new IReviewsCallback() {
            @Override
            public void onSuccess(List<Review> reviews) {
                List<Appointment> data = closedAppointmentsMutableLiveData.getValue().getData();
                data.get(data.indexOf(target)).setReview(reviews.get(0));
                closedAppointmentsMutableLiveData.postValue(new Result<>(data, type, "sendReview"));
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(TAG, throwable.toString());
                closedAppointmentsMutableLiveData.postValue(new Result<>(throwable, type, "sendReview"));
            }
        });

        return null;
    }

    public LiveData<Result<List<Appointment>>> getSentAppointmentsLiveData() {
        return sentAppointmentsMutableLiveData;
    }

    public LiveData<Result<List<Appointment>>> getRecvAppointmentsLiveData() {
        return recvAppointmentsMutableLiveData;
    }

    public LiveData<Result<List<Appointment>>> getClosedAppointmentsLiveData() {
        return closedAppointmentsMutableLiveData;
    }

    public void setTabIndex(Integer index) {
        this.tabIndex.setValue(index);
    }

    public LiveData<Integer> getTabIndexLiveData() {
        return tabIndex;
    }
}
