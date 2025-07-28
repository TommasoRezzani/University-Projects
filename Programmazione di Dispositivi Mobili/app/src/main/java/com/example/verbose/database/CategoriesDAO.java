package com.example.verbose.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.verbose.model.Category;

import java.util.List;
import java.util.Set;

@Dao
public interface CategoriesDAO {
    @Query("SELECT DISTINCT * FROM category")
    List<Category> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Set<Category> categories);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Category... categories);

    @Delete
    void delete(Category category);

    @Query("DELETE FROM category")
    void deleteAll();
}
