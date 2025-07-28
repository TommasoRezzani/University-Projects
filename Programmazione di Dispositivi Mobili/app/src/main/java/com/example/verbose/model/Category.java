package com.example.verbose.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.verbose.R;
import com.example.verbose.adapter.CategoryTypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

@Entity
@JsonAdapter(CategoryTypeAdapter.class)
public class Category implements Comparable<Category> {
    @PrimaryKey
    protected int id;
    protected final String name;
    @SerializedName("icon_url")
    protected final String iconUrl;

    public Category(int id, String name, String iconUrl){
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
    }
    @Ignore
    public Category(String name, String iconUrl){
        this.name = name;
        this.iconUrl = iconUrl;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    @NonNull
    @Override
    public String toString() {
        return "{" + name + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return id == category.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public int compareTo(Category o) {
        return Integer.compare(this.id, o.id);
    }
}
