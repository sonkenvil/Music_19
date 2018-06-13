package com.framgia.nguyenson.music_19.screen.main.genres;

import com.framgia.nguyenson.music_19.data.model.Song;
import com.framgia.nguyenson.music_19.data.repository.SongRepository;
import com.framgia.nguyenson.music_19.data.source.SongDataSource;

import java.util.List;

public class GenresPresenter implements GenresContract.Presenter,
        SongDataSource.OnFetchDataListener<Song> {
    private GenresContract.View mView;
    private SongRepository mSongRepository;

    public GenresPresenter(SongRepository source) {
        mSongRepository = source;
    }


    @Override
    public void loadMusic(String genre, int limit, int offset) {
        mSongRepository.getSongs(genre, limit, offset, this);
        mView.showProgress();
    }

    @Override
    public void setView(GenresContract.View view) {
        this.mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onFetchDataSuccess(List<Song> list) {
        mView.hideProgress();
        mView.showData(list);
    }

    @Override
    public void onFetchDataError(String message) {
        mView.hideProgress();
        mView.showError(message);
    }
}
