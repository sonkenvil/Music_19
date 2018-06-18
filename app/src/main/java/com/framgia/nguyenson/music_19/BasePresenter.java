package com.framgia.nguyenson.music_19;

public interface BasePresenter<T> {

    void setView(T view);

    void onStart();

    void onStop();
}
