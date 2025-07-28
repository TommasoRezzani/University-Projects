package com.example.verbose.ui.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.verbose.model.AuthUser;
import com.example.verbose.model.Repetition;
import com.example.verbose.model.Result;
import com.example.verbose.repository.RepetitionsRepository;
import com.example.verbose.repository.UsersRepository;
import com.example.verbose.sources.IRepetitionsCallback;
import com.example.verbose.sources.IUsersCallback;
import com.example.verbose.util.ServiceLocator;

import java.util.List;

public class SearchViewModel extends ViewModel {
    private String searchQuery;
    private MutableLiveData<Boolean> showFavorites;

    private final MutableLiveData<Result<AuthUser>> loggedUserMutableLiveData;
    private final MutableLiveData<Result<List<Repetition>>> favoritesMutableLiveData;
    private final MutableLiveData<Result<List<Repetition>>> searchResultsMutableLiveData;

    private final UsersRepository usersRepository;
    private final RepetitionsRepository repetitionsRepository;

    public SearchViewModel(){
        this.usersRepository = ServiceLocator.getInstance().getUsersRepository();
        this.repetitionsRepository = ServiceLocator.getInstance().getRepetitionsRepository();

        this.loggedUserMutableLiveData = new MutableLiveData<>();
        this.favoritesMutableLiveData = new MutableLiveData<>();
        this.searchResultsMutableLiveData = new MutableLiveData<>();

        this.showFavorites = new MutableLiveData<>(true);

        usersRepository.getLoggedUserData(new IUsersCallback() {
            @Override
            public void onSuccess(AuthUser authUser) {
                loggedUserMutableLiveData.postValue(new Result<>(authUser, Result.EventType.GET_USER, "SearchViewModel"));
            }

            @Override
            public void onFailure(Throwable throwable) {
                loggedUserMutableLiveData.postValue(new Result<>(throwable, Result.EventType.GET_USER, "SearchViewModel"));
            }
        });
    }

    public LiveData<Result<List<Repetition>>> getFavorites(){
        Result.EventType type = Result.EventType.GET_FAVORITES;

        usersRepository.getUserFavorites(new IRepetitionsCallback() {
            @Override
            public void onSuccess(List<Repetition> repetitions) {
                favoritesMutableLiveData.postValue(new Result<>(repetitions, type, "getFavorites"));
            }

            @Override
            public void onFailure(Throwable throwable) {
                favoritesMutableLiveData.postValue(new Result<>(throwable, type, "getFavorites"));
            }
        });

        return favoritesMutableLiveData;
    }

    public LiveData<Result<List<Repetition>>> search(String query){
        Result.EventType type = Result.EventType.GET_REPETITIONS;

        searchQuery = query;
        this.repetitionsRepository.searchRepetitions(query, new IRepetitionsCallback() {
            @Override
            public void onSuccess(List<Repetition> repetitions) {
                searchResultsMutableLiveData.postValue(new Result<>(repetitions, type, "search"));
            }

            @Override
            public void onFailure(Throwable throwable) {
                searchResultsMutableLiveData.postValue(new Result<>(throwable, type, "search"));
            }
        });

        showFavorites.setValue(false);

        return searchResultsMutableLiveData;
    }

    public MutableLiveData<Result<AuthUser>> getLoggedUserMutableLiveData() {
        return loggedUserMutableLiveData;
    }

    public LiveData<Result<List<Repetition>>> getFavoritesLiveData() {
        return favoritesMutableLiveData;
    }

    public LiveData<Result<List<Repetition>>> getSearchResultsLiveData() {
        return searchResultsMutableLiveData;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public LiveData<Boolean> showFavorites(){
        return showFavorites;
    }

    public void setShowFavorites(boolean showFavorites) {
        this.showFavorites.setValue(showFavorites);
    }

    public LiveData<Result<List<Repetition>>> getDisplayLiveData(){
        if(Boolean.TRUE.equals(showFavorites.getValue()))
            return favoritesMutableLiveData;
        else
            return searchResultsMutableLiveData;
    }
}
