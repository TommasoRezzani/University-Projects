package com.example.verbose.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.verbose.R;

import java.util.Arrays;
import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.ViewHolder> {
    private static final String TAG = AppointmentAdapter.class.getSimpleName();

    private final Boolean[] slots;

    public interface OnSlotClickedListener{
        void onClick(View v, Integer slot);
    }
    private OnSlotClickedListener onSlotClickedListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView cellText;
        private final CardView cardView;

        public ViewHolder(View view) {
            super(view);
            cellText = view.findViewById(R.id.slot_text);
            cardView = view.findViewById(R.id.slot_cardview);
        }

        public TextView getCellText() {
            return cellText;
        }

        public CardView getCardView() {
            return cardView;
        }
    }

    public TimeSlotAdapter(List<Integer> slots){
        this.slots = new Boolean[24];
        Arrays.fill(this.slots, false);
        slots.parallelStream().forEach(index -> this.slots[index] = true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slot_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView cellText = holder.getCellText();
        CardView cardView = holder.getCardView();

        cellText.setText(position + ":00");
        if(slots[position]){
            cellText.setTextColor(0xFFFFFFFF);
            cardView.setCardBackgroundColor(0xFF585F65);
        } else {
            cardView.setOnClickListener(v -> onSlotClickedListener.onClick(v, position));
            cardView.setCardBackgroundColor(0xFFFFFFFF);
        }
    }


    @Override
    public int getItemCount() {
        return slots.length;
    }

    public Boolean[] getSlots() {
        return slots;
    }

    public void setOnSlotClickedListener(OnSlotClickedListener onSlotClickedListener) {
        this.onSlotClickedListener = onSlotClickedListener;
    }
}
