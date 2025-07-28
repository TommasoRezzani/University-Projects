package com.example.verbose.adapter.AppointmentHolder;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.verbose.R;
import com.example.verbose.adapter.AppointmentViewHolder;

public class ReceivedAppointmentHolder extends AppointmentViewHolder {
    private final Button closeBtn;
    public ReceivedAppointmentHolder(@NonNull View view) {
        super(view);
        closeBtn = view.findViewById(R.id.close_appointment_button);
    }

    public Button getCloseBtn() {
        return closeBtn;
    }
}
