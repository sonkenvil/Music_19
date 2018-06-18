package com.framgia.nguyenson.music_19.screen.main.offline;

import com.framgia.nguyenson.music_19.data.model.Song;
import com.framgia.nguyenson.music_19.data.repository.SongRepository;
import com.framgia.nguyenson.music_19.data.source.SongDataSource;
import com.framgia.nguyenson.music_19.screen.main.genres.GenresContract;

import java.util.List;

public class MusicOfflinePresenter implements MusicOfflineContract.Presenter, SongDataSource.OnGetDataListener<Song> {
    private MusicOfflineContract.View mView;
    private SongRepository mRepository;

    public MusicOfflinePresenter(SongRepository songRepository) {
        mRepository = songRepository;
    }

    @Override
    public void getAllMusic() {
        mRepository.getSongs(this);
    }

    @Override
    public void setView(MusicOfflineContract.View view) {
        mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onGetDataSuccess(List<Song> list) {
        mView.showData(list);
    }

    @Override
    public void onGetDataError(String message) {
        mView.showError(message);
    }
}
