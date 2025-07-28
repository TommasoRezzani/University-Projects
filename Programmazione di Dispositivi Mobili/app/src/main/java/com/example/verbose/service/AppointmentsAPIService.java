package com.example.verbose.service;

import com.example.verbose.model.Appointment;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AppointmentsAPIService {
    @GET("appointments/{id}")
    Call<Appointment> getAppointment(@Path("id") String id);

    @POST("appointments/")
    Call<Appointment> createAppointment(@Header("Authorization") String idToken, @Body Appointment appointment);

    @PUT("appointments/{id}/accept")
    Call<Void> acceptAppointment(@Header("Authorization") String idToken, @Path("id") String id);

    @PUT("appointments/{id}/refuse")
    Call<Void> refuseAppointment(@Header("Authorization") String idToken, @Path("id") String id);

    @PUT("appointments/{id}/close")
    Call<Void> closeAppointment(@Header("Authorization") String idToken, @Path("id") String id);

    @DELETE("appointments/{id}/delete")
    Call<Void> deleteAppointment(@Header("Authorization") String idToken, @Path("id") String id);
}
