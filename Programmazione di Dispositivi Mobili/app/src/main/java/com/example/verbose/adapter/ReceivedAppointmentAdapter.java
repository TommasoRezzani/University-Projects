package com.example.verbose.adapter;

import com.example.verbose.model.Appointment;

import java.util.List;

public class ReceivedAppointmentAdapter extends AppointmentAdapter {
    public ReceivedAppointmentAdapter(Appointment[] appointments) {
        super(appointments);
    }

    public ReceivedAppointmentAdapter(List<Appointment> appointments) {
        super(appointments);
    }

    @Override
    public int getItemViewType(int position) {
        return AppointmentType.RECEIVED.getValue();
    }
}
