package com.example.verbose.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.verbose.R;
import com.example.verbose.model.Appointment;

public abstract class AppointmentViewHolder extends RecyclerView.ViewHolder {
    private Appointment appointment;
    private final TextView repetitionName;
    private final TextView username;
    private final TextView date;
    private final TextView time;
    private final TextView clientRequest;
    private final CardView cardView;
    private final Button deleteButton;
    public AppointmentViewHolder(@NonNull View view) {
        super(view);

        repetitionName = view.findViewById(R.id.repetition_name_text);
        username = view.findViewById(R.id.client_name_row);
        date = view.findViewById(R.id.appointment_date_text);
        time = view.findViewById(R.id.appointment_time_text);
        clientRequest = view.findViewById(R.id.client_request_text);
        cardView = view.findViewById(R.id.appointment_card);
        deleteButton = view.findViewById(R.id.delete_appointment_button);
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public TextView getRepetitionName() {
        return repetitionName;
    }

    public TextView getUsername() {
        return username;
    }

    public TextView getDate() {
        return date;
    }

    public TextView getTime() {
        return time;
    }

    public TextView getClientRequest() {
        return clientRequest;
    }

    public CardView getCardView() {
        return cardView;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}
