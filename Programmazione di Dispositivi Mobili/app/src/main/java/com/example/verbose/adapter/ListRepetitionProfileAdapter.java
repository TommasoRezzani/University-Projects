package com.example.verbose.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.example.verbose.R;
import com.example.verbose.model.Repetition;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Comparator;
import java.util.List;

public class ListRepetitionProfileAdapter extends RecyclerView.Adapter<ListRepetitionProfileAdapter.ViewHolder> {

    public interface OnRepetitionCloseListener {
        void onClose(View v, int position,Repetition repetition);
    }

    public interface OnRepetitionClickListener {
        void onClick(View v, Repetition repetition);
    }

    private List<Repetition> repetitions;
    private OnRepetitionCloseListener onCloseListener;
    private OnRepetitionClickListener onClickListener;
    private boolean showOpen;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView nomeMateria;
        private final TextView categoria;
        private final TextView rangeOrario;
        private final RatingBar ratingBar;
        private final TextView numeroPrenotazioniAttive;
        private Repetition repetition;
        private CardView cardView;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeMateria = itemView.findViewById(R.id.text_view_titolo_lro);
            categoria = itemView.findViewById(R.id.text_view_categoria_lro);
            rangeOrario = itemView.findViewById(R.id.text_view_rangeorario_lro);
            ratingBar = itemView.findViewById(R.id.rating_bar_lro);
            numeroPrenotazioniAttive = itemView.findViewById(R.id.text_view_npa);
            cardView = itemView.findViewById(R.id.card_repetition_profile);
            imageView = itemView.findViewById(R.id.image_view_trash_lro);
        }

        public void setRepetition(Repetition repetition){
            this.repetition = repetition;
        }
        public Repetition getRepetition() { return repetition; }
        public TextView getNomeMateria() { return nomeMateria; }
        public TextView getCategoria() { return categoria; }
        public TextView getRangeOrario() { return rangeOrario; }
        public RatingBar getRatingBar() {return ratingBar; }
        public TextView getNumeroPrenotazioniAttive() {return numeroPrenotazioniAttive; }
        public CardView getCardView() { return cardView; }

        public ImageView getImageView() {return imageView; }
    }

    public ListRepetitionProfileAdapter(List<Repetition> repetitions){
        this.repetitions = repetitions;
        this.showOpen = true;
    }

    public ListRepetitionProfileAdapter(List<Repetition> repetitions, Comparator<Repetition> comparator){
        this.repetitions = repetitions;
        this.repetitions.sort(comparator);
        this.showOpen = true;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repetition_profile_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Repetition repetition = repetitions.get(position);

        if(showOpen != repetition.isClosed()){
            Context context = holder.itemView.getContext();

            holder.setRepetition(repetition);
            holder.getNomeMateria().setText(repetition.getName());
            holder.getCategoria().setText(repetition.getCategory().getName());
            holder.getRatingBar().setRating(repetition.getVote());
            holder.getNumeroPrenotazioniAttive().setText(context.getString(R.string.requests_cnt_format, repetition.getRequestsCnt()));
            holder.getRangeOrario().setText(String.format("%1$d:00 - %2$d:00", repetition.getStartTime(), repetition.getEndTime()));

            ImageView imageView = holder.getImageView();
            if(!repetition.isClosed()) {
                imageView.setOnClickListener(v -> onCloseListener.onClose(v, position,repetition));
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }

            CardView cardView = holder.getCardView();
            cardView.setOnClickListener(v -> this.onClickListener.onClick(v, repetition));
            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
    }

    @Override
    public int getItemCount() {
        return repetitions.size();
    }

    public void setOnCloseListener(OnRepetitionCloseListener onCloseListener) {
        this.onCloseListener = onCloseListener;
    }

    public void setOnClickListener(OnRepetitionClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void showOpen(int position) {
        switch (position){
            case 0:
                showOpen = true;
                break;
            case 1:
                showOpen = false;
                break;
        }
        this.notifyDataSetChanged();
    }
}
