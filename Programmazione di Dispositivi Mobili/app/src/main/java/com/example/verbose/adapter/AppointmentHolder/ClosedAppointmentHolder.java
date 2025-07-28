package com.example.verbose.adapter.AppointmentHolder;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.verbose.R;
import com.example.verbose.adapter.AppointmentViewHolder;

public class ClosedAppointmentHolder extends AppointmentViewHolder {
    private final Button leaveReviewBtn;

    public ClosedAppointmentHolder(@NonNull View view) {
        super(view);
        leaveReviewBtn = view.findViewById(R.id.review_appointment_button);
    }

    public Button getLeaveReviewBtn() {
        return leaveReviewBtn;
    }
}
