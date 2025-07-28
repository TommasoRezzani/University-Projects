package com.example.verbose.adapter.AppointmentHolder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.verbose.R;
import com.example.verbose.adapter.AppointmentViewHolder;

public class SentAppointmentHolder extends AppointmentViewHolder {
    private final ImageView stateIcon;

    public SentAppointmentHolder(@NonNull View view) {
        super(view);

        stateIcon = view.findViewById(R.id.state_image_view);
    }

    public ImageView getStateIcon() {
        return stateIcon;
    }
}
