package com.example.verbose.sources;

import android.util.Log;

import com.example.verbose.model.Repetition;

import java.util.List;

public interface IRepetitionsCallback {
    void onSuccess(List<Repetition> repetitions);
    void onFailure(Throwable throwable);
}
