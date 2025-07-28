package com.example.verbose.adapter;

import static android.icu.text.DateFormat.getPatternInstance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.verbose.R;
import com.example.verbose.adapter.NotificationHolder.AppointmentClosedHolder;
import com.example.verbose.adapter.NotificationHolder.AppointmentConfirmedHolder;
import com.example.verbose.adapter.NotificationHolder.AppointmentDeletedHolder;
import com.example.verbose.adapter.NotificationHolder.AppointmentNotificationHolder;
import com.example.verbose.adapter.NotificationHolder.AppointmentRefusedHolder;
import com.example.verbose.adapter.NotificationHolder.NotificationViewHolder;
import com.example.verbose.adapter.NotificationHolder.RequestNotificationHolder;
import com.example.verbose.model.Notification;

import java.util.Arrays;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {
    private static final String TAG = NotificationAdapter.class.getSimpleName();

    private final List<Notification> notifications;

    public interface OnItemDeletedListener {
        void onDelete(Notification notification);
    }

    public interface OnRequestAnsweredListener {
        void onAnswered(String targetObjectId, boolean accepted);
    }

    private OnItemDeletedListener onItemDeletedListener;
    private OnRequestAnsweredListener onRequestAnsweredListener;

    public NotificationAdapter(Notification[] notifications){
        this.notifications = Arrays.asList(notifications);
        this.notifications.sort((o1, o2) -> Long.compare(o2.getTime(), o1.getTime()));
    }

    public NotificationAdapter(List<Notification> notifications){
        this.notifications = notifications;
        this.notifications.sort((o1, o2) -> Long.compare(o2.getTime(), o1.getTime()));
    }

    @Override
    public int getItemViewType(int position) {
        return notifications.get(position).getType().getValue();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_notification_row, parent, false);
            return new RequestNotificationHolder(view);
        }
        if(viewType == 2) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_notification_row, parent, false);
            return new AppointmentNotificationHolder(view);
        }
        if(viewType == 3){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_deleted_row, parent, false);
            return new AppointmentDeletedHolder(view);
        }
        if(viewType == 4){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_confirmed_row, parent, false);
            return new AppointmentConfirmedHolder(view);
        }
        if(viewType == 5){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_refused_row, parent, false);
            return new AppointmentRefusedHolder(view);
        }
        if(viewType == 6){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_closed_row, parent, false);
            return new AppointmentClosedHolder(view);
        }

        throw new RuntimeException();
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        //TODO bug eliminazione notifiche(NullPointerException)
        holder.getRemoveButton().setOnClickListener(v -> deleteItem(position));

        holder.getTitle().setText(notifications.get(position).getTitle());
        holder.getContent().setText(notifications.get(position).getContent());
        try {
            holder.getTime().setText(getPatternInstance("yyMMdHm").format(notifications.get(position).getTime()));
        } catch (NullPointerException ignored){}

        if(holder instanceof RequestNotificationHolder){
            RequestNotificationHolder requestNotificationHolder = (RequestNotificationHolder) holder;
            requestNotificationHolder.getAcceptBtn().setOnClickListener(v -> {
                onRequestAnsweredListener.onAnswered(notifications.get(position).getTargetObjectId(), true);
                deleteItem(position);
            });
            requestNotificationHolder.getRefuseBtn().setOnClickListener(v -> {
                onRequestAnsweredListener.onAnswered(notifications.get(position).getTargetObjectId(), false);
                deleteItem(position);
            });
        }
        if(holder instanceof AppointmentNotificationHolder){
            AppointmentNotificationHolder appointmentHolder = (AppointmentNotificationHolder) holder;
            return;
        }
        if(holder instanceof AppointmentDeletedHolder){
            AppointmentDeletedHolder appointmentHolder = (AppointmentDeletedHolder) holder;
            return;
        }
        if(holder instanceof AppointmentConfirmedHolder){
            AppointmentConfirmedHolder appointmentHolder = (AppointmentConfirmedHolder) holder;
            return;
        }
        if(holder instanceof AppointmentRefusedHolder){
            AppointmentRefusedHolder appointmentHolder = (AppointmentRefusedHolder) holder;
            return;
        }
        if(holder instanceof AppointmentClosedHolder){
            AppointmentClosedHolder appointmentHolder = (AppointmentClosedHolder) holder;
            return;
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void setOnItemDeletedListener(OnItemDeletedListener listener){
        this.onItemDeletedListener = listener;
    }

    public void setOnRequestAnsweredListener(OnRequestAnsweredListener listener) {
        this.onRequestAnsweredListener = listener;
    }

    private void deleteItem(int position){
        this.onItemDeletedListener.onDelete(notifications.get(position));
        notifications.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, notifications.size());
    }
}
