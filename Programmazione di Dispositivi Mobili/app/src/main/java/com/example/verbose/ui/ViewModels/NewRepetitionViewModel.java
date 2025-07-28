package com.example.verbose.ui.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.verbose.model.Repetition;
import com.example.verbose.model.Result;
import com.example.verbose.repository.UsersRepository;
import com.example.verbose.sources.IRepetitionsCallback;
import com.example.verbose.util.ServiceLocator;

import java.util.List;

public class NewRepetitionViewModel extends ViewModel {
    private final UsersRepository usersRepository;

    private final MutableLiveData<Result<Repetition>> creationResult;

    public NewRepetitionViewModel(){
        usersRepository = ServiceLocator.getInstance().getUsersRepository();

        creationResult = new MutableLiveData<>();
    }

    public LiveData<Result<Repetition>> createRepetition(Repetition repetition){
        Result.EventType type = Result.EventType.GET_REPETITIONS;
        this.usersRepository.createUserRepetition(repetition, new IRepetitionsCallback() {
            @Override
            public void onSuccess(List<Repetition> repetitions) {
                creationResult.postValue(new Result<>(repetitions.get(0), type, "createRepetition"));
            }

            @Override
            public void onFailure(Throwable throwable) {
                creationResult.postValue(new Result<>(throwable, type, "createRepetition"));
            }
        });

        return creationResult;
    }

    public MutableLiveData<Result<Repetition>> getCreationResult() {
        return creationResult;
    }
}
