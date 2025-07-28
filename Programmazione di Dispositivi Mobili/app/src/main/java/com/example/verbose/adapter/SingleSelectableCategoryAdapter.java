package com.example.verbose.adapter;

import android.widget.CheckBox;

import androidx.annotation.NonNull;

import com.example.verbose.model.Category;
import com.example.verbose.model.SelectableCategory;

import java.util.List;
import java.util.Optional;

public class SingleSelectableCategoryAdapter extends SelectableCategoryAdapter {

    private int selectedIndex;
    public SingleSelectableCategoryAdapter(SelectableCategory[] categories) {
        super(categories);
        this.selectedIndex = -1;
    }

    public SingleSelectableCategoryAdapter(List<SelectableCategory> categories) {
        super(categories);
        this.selectedIndex = -1;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //super.onBindViewHolder(holder, position);

        CheckBox checkBox = holder.getCheckBox();
        checkBox.setText(categories.get(position).getName());
        checkBox.setChecked(categories.get(position).isSelected());
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                if (selectedIndex != -1) {
                    categories.get(selectedIndex).setSelected(false);
                    notifyItemChanged(selectedIndex);
                }
                selectedIndex = holder.getAdapterPosition();
                buttonView.setSelected(true);
            } /*else {
                selectedIndex = -1;
                buttonView.setSelected(false);
            }*/
        });
    }

    public Optional<Category> getSelectedCategory() {
        return Optional.ofNullable(selectedIndex != -1 ? categories.get(selectedIndex) : null);
    }
}
