package com.example.verbose.model;

import androidx.annotation.NonNull;

public class SelectableCategory extends Category {
    private transient boolean selected;

    public SelectableCategory(Category category){
        super(category.getId(), category.getName(), category.getIconUrl());
        this.selected = false;
    }

    public SelectableCategory(Category category, boolean selected){
        super(category.getId(), category.getName(), category.getIconUrl());
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @NonNull
    @Override
    public String toString() {
        return "{" + name + ", "+ selected + '}';
    }
}
