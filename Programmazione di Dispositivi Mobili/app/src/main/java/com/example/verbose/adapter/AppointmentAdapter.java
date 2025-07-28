package com.example.verbose.adapter;

import static android.icu.text.DateFormat.getPatternInstance;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.verbose.R;
import com.example.verbose.adapter.AppointmentHolder.ClosedAppointmentHolder;
import com.example.verbose.adapter.AppointmentHolder.ReceivedAppointmentHolder;
import com.example.verbose.adapter.AppointmentHolder.SentAppointmentHolder;
import com.example.verbose.model.Appointment;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class AppointmentAdapter extends RecyclerView.Adapter<AppointmentViewHolder> {
    private static final String TAG = AppointmentAdapter.class.getSimpleName();

    private final List<Appointment> appointments;

    public interface OnClickListener{
        void onClick(View v, Appointment target);
    }

    public interface OnDeleteListener{
        void onDelete(View v, Appointment target);
    }

    public interface OnCloseListener{
        void onClose(View v, Appointment target);
    }

    public interface OnLeaveReviewListener{
        void onLeaveReview(View v, Appointment target);
    }

    private OnClickListener onClickListener;
    private OnDeleteListener onDeleteListener;
    private OnCloseListener onCloseListener;
    private OnLeaveReviewListener onLeaveReviewListener;

    public enum AppointmentType{
        RECEIVED(0),
        SENT(1),
        CLOSED(2);

        private int value;
        AppointmentType(int value){
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Appointment appointment;
        private final TextView repetitionName;
        private final TextView username;
        private final TextView date;
        private final TextView time;
        private final TextView clientRequest;
        private final CardView cardView;
        private final Button deleteButton;

        private AppointmentType type;

        public ViewHolder(View view, AppointmentType type) {
            super(view);

            this.type = type;
            repetitionName = view.findViewById(R.id.repetition_name_text);
            username = view.findViewById(R.id.client_name_row);
            date = view.findViewById(R.id.appointment_date_text);
            time = view.findViewById(R.id.appointment_time_text);
            clientRequest = view.findViewById(R.id.client_request_text);
            cardView = view.findViewById(R.id.appointment_card);
            deleteButton = view.findViewById(R.id.delete_appointment_button);
        }

        public void setAppointment(Appointment repetition) {
            this.appointment = repetition;
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

        public AppointmentType getType() {
            return type;
        }
    }

    public AppointmentAdapter(@NonNull Appointment[] appointments){
        this.appointments = Arrays.asList(appointments);
    }

    public AppointmentAdapter(@NonNull List<Appointment> appointments){
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == AppointmentType.RECEIVED.value){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.received_appointment_row, parent, false);
            return new ReceivedAppointmentHolder(view);
        } else if (viewType == AppointmentType.SENT.value) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_appointment_row, parent, false);
            return new SentAppointmentHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.closed_appointment_row, parent, false);
            return new ClosedAppointmentHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        holder.setAppointment(appointments.get(position));

        TextView repetitionName = holder.getRepetitionName();
        TextView username = holder.getUsername();
        TextView date = holder.getDate();
        TextView time = holder.getTime();
        TextView clientRequest = holder.getClientRequest();
        CardView cardView = holder.getCardView();
        Button deleteButton = holder.getDeleteButton();

        Appointment appointment = appointments.get(position);

        repetitionName.setText(appointment.getRepetitionInfo().getName());
        date.setText(getPatternInstance("yyyyMMd").format(appointment.getDate()));
        time.setText(String.format("%1$s:00", appointment.getTimeSlot()));
        clientRequest.setText(appointment.getRequest());

        if(holder instanceof ReceivedAppointmentHolder || holder instanceof SentAppointmentHolder){
            deleteButton.setOnClickListener(v -> this.onDeleteListener.onDelete(v, appointment));
            cardView.setOnClickListener(v -> this.onClickListener.onClick(v, appointment));
        }

        if(holder instanceof SentAppointmentHolder){
            ImageView stateIcon = ((SentAppointmentHolder) holder).getStateIcon();
            if(appointment.isAccepted()) {
                stateIcon.setImageResource(R.drawable.accepted_request_icon);
                stateIcon.setColorFilter(stateIcon.getContext().getColor(R.color.verbose_confirm_color));
                stateIcon.setColorFilter(0xFF43A047);
            } else {
                stateIcon.setImageResource(R.drawable.refused_request_icon);
                stateIcon.setColorFilter(stateIcon.getContext().getColor(com.google.android.material.R.color.design_default_color_error));
            }
        }

        if(holder instanceof ReceivedAppointmentHolder) {
            username.setText(username.getContext().getString(R.string.with_format, appointment.getClientInfo().getUsername()));
            Button closeBtn = ((ReceivedAppointmentHolder) holder).getCloseBtn();
            closeBtn.setOnClickListener(v -> this.onCloseListener.onClose(v, appointment));
            Calendar current = Calendar.getInstance();
            current.setTime(new Date());
            Calendar repDate = Calendar.getInstance();
            repDate.setTime(appointment.getDate());
            repDate.add(Calendar.HOUR, appointment.getTimeSlot());

            if(current.after(repDate))
                deleteButton.setVisibility(View.INVISIBLE);
            else
                deleteButton.setVisibility(View.VISIBLE);
        }
        else
            username.setText(username.getContext().getString(R.string.with_format,appointment.getRepetitionInfo().getOwnerInfo().getUsername()));

        if(holder instanceof ClosedAppointmentHolder){
            Button leaveReviewBtn = ((ClosedAppointmentHolder) holder).getLeaveReviewBtn();
            if(appointment.getReview().isPresent()){
                leaveReviewBtn.setText(leaveReviewBtn.getContext().getString(R.string.modify_review));
            } else {
                leaveReviewBtn.setText(leaveReviewBtn.getContext().getString(R.string.leave_review));
            }
            leaveReviewBtn.setOnClickListener(v -> this.onLeaveReviewListener.onLeaveReview(v, appointment));
        }
    }


    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    public void setOnCloseListener(OnCloseListener onCloseListener) {
        this.onCloseListener = onCloseListener;
    }

    public void setOnLeaveReviewListener(OnLeaveReviewListener onLeaveReviewListener) {
        this.onLeaveReviewListener = onLeaveReviewListener;
    }
}
