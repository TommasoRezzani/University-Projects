package com.example.verbose.sources;

import android.util.Log;

import com.example.verbose.model.Appointment;
import com.example.verbose.service.AppointmentsAPIService;
import com.example.verbose.util.ServiceLocator;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestAppointmentsDataSource {
    private static final String TAG = RestAppointmentsDataSource.class.getSimpleName();
    private AppointmentsAPIService appointmentsAPIService;

    public RestAppointmentsDataSource(){
        this.appointmentsAPIService = ServiceLocator.getInstance().getAppointmentsAPIService();
    }

    public void getAppointment(String id, IAppointmentsCallback callback){
        this.appointmentsAPIService.getAppointment(id).enqueue(new Callback<Appointment>() {
            @Override
            public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                Log.d(TAG, response.body().getId());
                List<Appointment> appointments = new ArrayList<>();
                appointments.add(response.body());
                callback.onSuccess(appointments);
            }

            @Override
            public void onFailure(Call<Appointment> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
                callback.onFailure(throwable);
            }
        });
    }

    public void createAppointment(String idToken, Appointment appointment, IAppointmentsCallback callback){
        assert appointment.getId() == null;

        this.appointmentsAPIService.createAppointment(idToken, appointment).enqueue(new Callback<Appointment>() {
            @Override
            public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.body().getId());
                    List<Appointment> appointments = new ArrayList<>();
                    appointments.add(response.body());
                    callback.onSuccess(appointments);
                } else {
                    try {
                        String json = response.errorBody().string();
                        callback.onFailure(new Exception(new GsonBuilder().create().fromJson(json, Map.class).get("detail").toString()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Appointment> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
                callback.onFailure(throwable);
            }
        });
    }

    public void acceptAppointment(String idToken, String appointmentId, IAppointmentsCallback callback){
        this.appointmentsAPIService.acceptAppointment(idToken, appointmentId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, response.message());
                callback.onSuccess(null);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
                callback.onFailure(throwable);
            }
        });
    }

    public void refuseAppointment(String idToken, String appointmentId, IAppointmentsCallback callback){
        this.appointmentsAPIService.refuseAppointment(idToken, appointmentId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, response.message());
                callback.onSuccess(null);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
                callback.onFailure(throwable);
            }
        });
    }

    public void closeAppointment(String idToken, String appointmentId, IAppointmentsCallback callback){
        this.appointmentsAPIService.closeAppointment(idToken, appointmentId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, response.message());
                callback.onSuccess(null);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
                callback.onFailure(throwable);
            }
        });
    }

    public void deleteAppointment(String idToken, Appointment appointment){
        assert appointment.getId() != null;

        this.appointmentsAPIService.deleteAppointment(idToken, appointment.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Log.d(TAG, throwable.toString());
            }
        });
    }
}
