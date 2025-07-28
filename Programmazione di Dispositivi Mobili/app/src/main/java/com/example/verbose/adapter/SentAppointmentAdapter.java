package com.example.verbose.adapter;

import com.example.verbose.model.Appointment;

import java.util.List;

public class SentAppointmentAdapter extends AppointmentAdapter {
    public SentAppointmentAdapter(Appointment[] appointments) {
        super(appointments);
    }

    public SentAppointmentAdapter(List<Appointment> appointments) {
        super(appointments);
    }

    @Override
    public int getItemViewType(int position) {
        return AppointmentType.SENT.getValue();
    }
}
