package com.example.verbose.adapter;

import com.example.verbose.model.Appointment;

import java.util.List;

public class ClosedAppointmentAdapter extends AppointmentAdapter {
    public ClosedAppointmentAdapter(Appointment[] appointments) {
        super(appointments);
    }

    public ClosedAppointmentAdapter(List<Appointment> appointments) {
        super(appointments);
    }

    @Override
    public int getItemViewType(int position) {
        return AppointmentType.CLOSED.getValue();
    }
}
