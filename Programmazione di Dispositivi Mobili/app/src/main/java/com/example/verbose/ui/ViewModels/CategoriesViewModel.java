package com.example.verbose.ui.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.verbose.model.Category;
import com.example.verbose.model.Result;
import com.example.verbose.repository.CategoriesRepository;
import com.example.verbose.sources.ICategoriesCallback;
import com.example.verbose.util.ServiceLocator;

import java.util.Set;

public class CategoriesViewModel extends ViewModel {
    private final MutableLiveData<Result<Set<Category>>> categoriesMutableLiveData;

    private final CategoriesRepository categoriesRepository;

    public CategoriesViewModel(Context context){
        categoriesMutableLiveData = new MutableLiveData<>();

        categoriesRepository = ServiceLocator.getInstance().getCategoriesRepository(context);
    }

    public LiveData<Result<Set<Category>>> getCategories(){
        Result.EventType type = Result.EventType.GET_CATEGORIES;
        this.categoriesRepository.getCategories(new ICategoriesCallback() {
            @Override
            public void onSuccess(Set<Category> categories) {
                categoriesMutableLiveData.postValue(new Result<>(categories, type, "getCategories"));
            }

            @Override
            public void onFailure(Throwable throwable) {
                categoriesMutableLiveData.postValue(new Result<>(throwable, type, "getCategories"));
            }
        });

        return categoriesMutableLiveData;
    }
}
