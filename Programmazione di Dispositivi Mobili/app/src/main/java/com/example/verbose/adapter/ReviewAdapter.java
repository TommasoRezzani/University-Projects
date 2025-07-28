package com.example.verbose.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.verbose.R;
import com.example.verbose.model.Review;
import com.example.verbose.util.GlideApp;

import java.util.Arrays;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private static final String TAG = ReviewAdapter.class.getSimpleName();

    private List<Review> reviews;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView authorPic;
        private final TextView authorNameText;
        private final RatingBar ratingBar;
        private final TextView contentText;

        public ViewHolder(@NonNull View view) {
            super(view);

            authorPic = view.findViewById(R.id.author_profile_pic);
            authorNameText = view.findViewById(R.id.author_name_text);
            ratingBar = view.findViewById(R.id.rating);
            contentText = view.findViewById(R.id.review_content_text);
        }

        public ImageView getAuthorPic() {
            return authorPic;
        }

        public TextView getAuthorNameText() {
            return authorNameText;
        }

        public RatingBar getRatingBar() {
            return ratingBar;
        }

        public TextView getContentText() {
            return contentText;
        }
    }

    public ReviewAdapter(Review[] reviews){
        this.reviews = Arrays.asList(reviews);
    }

    public ReviewAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        ImageView imageView = holder.getAuthorPic();
        TextView authorNameText = holder.getAuthorNameText();
        RatingBar ratingBar = holder.getRatingBar();
        TextView contentText = holder.getContentText();

        Review review = reviews.get(position);

        authorNameText.setText(review.getAuthorInfo().getUsername());
        ratingBar.setRating(review.getVote());
        contentText.setText(review.getContent());

        GlideApp.with(imageView)
                .load(review.getAuthorInfo().getProfilePicture())
                .placeholder(R.drawable.baseline_account_circle_24)
                .circleCrop()
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}
