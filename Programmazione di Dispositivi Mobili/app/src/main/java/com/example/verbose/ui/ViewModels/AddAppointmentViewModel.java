package com.example.verbose.ui.ViewModels;

import android.icu.util.Calendar;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.verbose.model.Appointment;
import com.example.verbose.model.AuthUser;
import com.example.verbose.model.Repetition;
import com.example.verbose.model.Result;
import com.example.verbose.model.Review;
import com.example.verbose.model.User;
import com.example.verbose.repository.RepetitionsRepository;
import com.example.verbose.repository.UsersRepository;
import com.example.verbose.sources.IAppointmentsCallback;
import com.example.verbose.sources.IReviewsCallback;
import com.example.verbose.sources.ISlotsCallback;
import com.example.verbose.sources.IUsersCallback;
import com.example.verbose.util.ServiceLocator;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class AddAppointmentViewModel extends ViewModel {
    private static final String TAG = AddAppointmentViewModel.class.getSimpleName();

    private final Repetition repetition;

    private final UsersRepository usersRepository;
    private final RepetitionsRepository repetitionsRepository;

    private final MutableLiveData<Result<List<Integer>>> slotsMutableLiveData;
    private final MutableLiveData<Date> currentDateMutableLiveData;
    private final MutableLiveData<Result<Appointment>> resultMutableLiveData;
    private final MutableLiveData<Result<List<Review>>> reviewsMutableLiveData;
    private final MutableLiveData<Result<AuthUser>> ownerMutableLiveData;

    public AddAppointmentViewModel(Repetition repetition){
        this.repetition = repetition;

        usersRepository = ServiceLocator.getInstance().getUsersRepository();
        repetitionsRepository = ServiceLocator.getInstance().getRepetitionsRepository();

        this.slotsMutableLiveData = new MutableLiveData<>();
        this.currentDateMutableLiveData = new MutableLiveData<>();
        this.resultMutableLiveData = new MutableLiveData<>();
        this.reviewsMutableLiveData = new MutableLiveData<>();
        this.ownerMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<Result<List<Integer>>> getSlotsByDate(Date date){
        if(!this.currentDateMutableLiveData.isInitialized())
            this.currentDateMutableLiveData.setValue(date);

        Result.EventType type = Result.EventType.GET_SLOTS;
        this.repetitionsRepository.getRepetitionSlotsByDate(repetition, date, new ISlotsCallback() {
            @Override
            public void onSuccess(List<Integer> slots) {
                IntStream.range(0, repetition.getStartTime()).boxed().forEach(slots::add);
                IntStream.range(repetition.getEndTime(), 24).boxed().forEach(slots::add);
                slotsMutableLiveData.postValue(new Result<>(slots, type, "getSlotsByData"));
            }

            @Override
            public void onFailure(Throwable throwable) {
                slotsMutableLiveData.postValue(new Result<>(throwable, type, "getSlotsByData"));
            }
        });

        return slotsMutableLiveData;
    }

    public LiveData<Date> setDate(Date date){
        this.currentDateMutableLiveData.setValue(date);

        return this.currentDateMutableLiveData;
    }

    public LiveData<Date> prevDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date minDate = calendar.getTime();
        if(currentDateMutableLiveData.getValue().after(minDate)) {
            calendar.setTime(this.currentDateMutableLiveData.getValue());
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            this.currentDateMutableLiveData.setValue(calendar.getTime());
        }

        return currentDateMutableLiveData;
    }

    public LiveData<Date> nextDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.currentDateMutableLiveData.getValue());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        this.currentDateMutableLiveData.setValue(calendar.getTime());

        return currentDateMutableLiveData;
    }

    public LiveData<Result<Appointment>> createAppointment(Integer slot, String requestText) {
        Result.EventType type = Result.EventType.SENT_APPOINTMENT;
        this.usersRepository.addUserAppointment(new Appointment(repetition.getRepetitionInfo(), requestText, currentDateMutableLiveData.getValue(), slot), new IAppointmentsCallback() {
            @Override
            public void onSuccess(List<Appointment> appointments) {
                getSlotsByDate(appointments.get(0).getDate());
                resultMutableLiveData.postValue(new Result<>(appointments.get(0), type, ""));

                appointments.forEach(appointment -> Log.d(TAG, appointment.getRequest()));
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d("TAG", throwable.toString());
                resultMutableLiveData.postValue(new Result<>(throwable, type, ""));
            }
        });

        return this.resultMutableLiveData;
    }

    public LiveData<Result<List<Review>>> getReviews() {
        Result.EventType type = Result.EventType.GET_REVIEWS;

        this.repetitionsRepository.getRepetitionReviews(repetition, new IReviewsCallback() {
            @Override
            public void onSuccess(List<Review> reviews) {
                reviewsMutableLiveData.postValue(new Result<>(reviews, type, "getReviews"));
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(TAG, throwable.toString());
                reviewsMutableLiveData.postValue(new Result<>(throwable, type, "getReviews"));
            }
        });

        return reviewsMutableLiveData;
    }

    public LiveData<Result<AuthUser>> getOwnerInfo(){
        Result.EventType type = Result.EventType.GET_USER;

        this.usersRepository.getUserData(this.repetition.getOwner().getUid(), new IUsersCallback() {
            @Override
            public void onSuccess(AuthUser authUser) {
                ownerMutableLiveData.postValue(new Result<>(authUser, type, "getOwnerInfo"));
            }

            @Override
            public void onFailure(Throwable throwable) {
                ownerMutableLiveData.postValue(new Result<>(throwable, type, "getOwnerInfo"));
            }
        });

        return ownerMutableLiveData;
    }

    public Repetition getRepetition() {
        return repetition;
    }

    public LiveData<Result<List<Integer>>> getSlotsLiveData() {
        return slotsMutableLiveData;
    }

    public LiveData<Date> getCurrentDateLiveData() {
        return currentDateMutableLiveData;
    }

    public LiveData<Result<Appointment>> getResultLiveData() {
        return resultMutableLiveData;
    }

    public LiveData<Result<AuthUser>> getOwnerLiveData() {
        return ownerMutableLiveData;
    }
}
