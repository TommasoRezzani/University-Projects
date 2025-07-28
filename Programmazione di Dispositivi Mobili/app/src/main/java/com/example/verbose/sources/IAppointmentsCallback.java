package com.example.verbose.sources;

import com.example.verbose.model.Appointment;

import java.util.List;

public interface IAppointmentsCallback {
    void onSuccess(List<Appointment> appointments);
    void onFailure(Throwable throwable);
}
