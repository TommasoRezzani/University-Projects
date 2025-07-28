package com.example.verbose.sources;

import android.content.Context;

import com.example.verbose.database.CategoriesDAO;
import com.example.verbose.database.VerboseRoomDatabase;
import com.example.verbose.model.Category;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class LocalCategoriesDataSource {
    private final CategoriesDAO categoriesDAO;

    public LocalCategoriesDataSource(Context context){
        this.categoriesDAO = VerboseRoomDatabase.getDatabase(context).categoriesDao();
    }

    public void getCategories(ICategoriesCallback callback){
        VerboseRoomDatabase.databaseWriteExecutor.execute(() -> {
            callback.onSuccess(new HashSet<>(this.categoriesDAO.getAll()));
        });
    }

    public void insertCategories(Set<Category> categories, ICategoriesCallback callback){
        VerboseRoomDatabase.databaseWriteExecutor
                .execute(() -> {
                    this.categoriesDAO.insertAll(categories);
                    callback.onSuccess(categories);
                });
    }

    public void insertCategories(ICategoriesCallback callback, Category... categories){
        VerboseRoomDatabase.databaseWriteExecutor
                .execute(() -> {
                    this.categoriesDAO.insertAll(categories);
                    callback.onSuccess(Arrays.stream(categories).collect(Collectors.toSet()));
                });
    }

    public void deleteCategory(Category category){
        VerboseRoomDatabase.databaseWriteExecutor
                .execute(() -> this.categoriesDAO.delete(category));
    }

    public void deleteAll(){
        VerboseRoomDatabase.databaseWriteExecutor.execute(this.categoriesDAO::deleteAll);
    }
}
