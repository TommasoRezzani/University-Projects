package com.example.verbose.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.verbose.R;
import com.example.verbose.model.SelectableCategory;

import java.util.Arrays;
import java.util.List;

public class SelectableCategoryAdapter extends RecyclerView.Adapter<SelectableCategoryAdapter.ViewHolder> {

    protected List<SelectableCategory> categories;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox checkBox;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            checkBox = view.findViewById(R.id.text_category_selection);
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }
    }

    public SelectableCategoryAdapter(SelectableCategory[] categories){
        this.categories = Arrays.asList(categories);
    }

    public SelectableCategoryAdapter(List<SelectableCategory> categories){
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CheckBox checkBox = holder.getCheckBox();
        checkBox.setText(categories.get(position).getName());
        checkBox.setChecked(categories.get(position).isSelected());
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> categories.get(holder.getAdapterPosition()).setSelected(isChecked));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public List<SelectableCategory> getCategories() {
        return categories;
    }
}
