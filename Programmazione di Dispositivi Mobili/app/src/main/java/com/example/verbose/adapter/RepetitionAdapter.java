package com.example.verbose.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.verbose.R;
import com.example.verbose.model.Repetition;
import com.example.verbose.util.GlideApp;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class RepetitionAdapter extends RecyclerView.Adapter<RepetitionAdapter.ViewHolder> {
    private static final String TAG = RepetitionAdapter.class.getSimpleName();

    private List<Repetition> repetitions;

    public interface OnRepetitionClickListener{
        void onClick(View v, Repetition repetition);
    }
    private OnRepetitionClickListener onRepetitionClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Repetition repetition;
        private final TextView repetitionName;
        private final TextView categoryName;
        private final TextView ownerName;
        private final RatingBar rating;
        private final TextView reviewCount;
        private final TextView timeRange;
        private final CardView cardView;
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            repetitionName = view.findViewById(R.id.subject_name_row);
            categoryName = view.findViewById(R.id.category_name_text);
            ownerName = view.findViewById(R.id.username_row);
            rating = view.findViewById(R.id.rating);
            reviewCount = view.findViewById(R.id.review_count);
            timeRange = view.findViewById(R.id.time_range_text);
            cardView = view.findViewById(R.id.search_card);
            imageView = view.findViewById(R.id.category_image_view);
        }

        public void setRepetition(Repetition repetition) {
            this.repetition = repetition;
        }

        public Repetition getRepetition() {
            return repetition;
        }

        public TextView getRepetitionName() {
            return repetitionName;
        }

        public TextView getCategoryName() {
            return categoryName;
        }

        public TextView getOwnerName() {
            return ownerName;
        }

        public RatingBar getRating() {
            return rating;
        }

        public TextView getReviewCount() {
            return reviewCount;
        }

        public TextView getTimeRange() {
            return timeRange;
        }

        public CardView getCardView() {
            return cardView;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

    public RepetitionAdapter(Repetition[] repetitions){
        this.repetitions = Arrays.asList(repetitions);
    }

    public RepetitionAdapter(List<Repetition> repetitions){
        this.repetitions = repetitions;
    }

    public RepetitionAdapter(List<Repetition> repetitions, Comparator<Repetition> comparator){
        this.repetitions = repetitions;
        this.repetitions.sort(comparator);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView repetitionName = holder.getRepetitionName();
        TextView categoryName = holder.getCategoryName();
        TextView ownerName = holder.getOwnerName();
        RatingBar rating = holder.getRating();
        TextView reviewCount = holder.getReviewCount();
        TextView timeRange = holder.getTimeRange();
        ImageView imageView = holder.getImageView();

        Repetition repetition = repetitions.get(position);
        Context context = holder.getImageView().getContext();

        holder.setRepetition(repetition);

        repetitionName.setText(repetition.getName());
        categoryName.setText(repetition.getCategory().getName());
        ownerName.setText(repetition.getOwner().getUsername());
        rating.setRating(repetition.getVote());
        reviewCount.setText(context.getString(R.string.review_cnt_format, repetition.getReviewsCnt()));
        timeRange.setText(String.format("%1$d:00 - %2$d:00", repetition.getStartTime(), repetition.getEndTime()));

        GlideApp.with(imageView.getContext())
                .load(repetition.getOwner().getProfilePicture())
                .placeholder(R.drawable.baseline_account_circle_24)
                .circleCrop()
                .into(imageView);

        holder.getCardView().setOnClickListener(v -> {
            this.onRepetitionClickListener.onClick(v, repetition);
        });
    }

    public void setOnRepetitionClickListener(OnRepetitionClickListener onRepetitionClickListener) {
        this.onRepetitionClickListener = onRepetitionClickListener;
    }

    @Override
    public int getItemCount() {
        return repetitions.size();
    }

    public List<Repetition> getRepetitions() {
        return repetitions;
    }
}
