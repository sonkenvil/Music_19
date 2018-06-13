package com.framgia.nguyenson.music_19.screen.main.genres;

import com.framgia.nguyenson.music_19.BasePresenter;
import com.framgia.nguyenson.music_19.BaseView;
import com.framgia.nguyenson.music_19.data.model.Song;

import java.util.List;

public interface GenresContract {
    interface View extends BaseView {

        void showProgress();

        void hideProgress();

        void showData(List<Song> list);

        void showError(String message);

    }

    interface Presenter extends BasePresenter<View> {
        void loadMusic(String genre,int limit, int offset);
    }
}
