package com.example.verbose.sources;

import java.util.List;

public interface ISlotsCallback {
    void onSuccess(List<Integer> slots);
    void onFailure(Throwable throwable);
}
