package com.example.verbose.adapter.NotificationHolder;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.verbose.R;

public class RequestNotificationHolder extends NotificationViewHolder {
    private final Button refuseBtn;
    private final Button acceptBtn;

    public RequestNotificationHolder(@NonNull View view) {
        super(view);

        refuseBtn = view.findViewById(R.id.request_refuse_button);
        acceptBtn = view.findViewById(R.id.request_accept_button);
    }

    public Button getAcceptBtn() {
        return acceptBtn;
    }

    public Button getRefuseBtn() {
        return refuseBtn;
    }
}
