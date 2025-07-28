package com.example.verbose.adapter.NotificationHolder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.verbose.R;

public abstract class NotificationViewHolder extends RecyclerView.ViewHolder {
    protected final TextView title;
    protected final TextView content;
    protected final TextView time;
    protected final ImageButton removeButton;
    protected final CardView cardView;

    public NotificationViewHolder(@NonNull View view) {
        super(view);

        title = view.findViewById(R.id.notification_title_text);
        content = view.findViewById(R.id.notification_text);
        time = view.findViewById(R.id.time_notification_text);
        removeButton = view.findViewById(R.id.delete_notification_button);
        cardView = view.findViewById(R.id.notification_card);
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getContent() {
        return content;
    }

    public ImageButton getRemoveButton() {
        return removeButton;
    }

    public void setBackgroudColor(int color){
        cardView.setCardBackgroundColor(color);
    }

    public TextView getTime() {
        return time;
    }
}
