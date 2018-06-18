package com.framgia.nguyenson.music_19.screen.music;

import com.framgia.nguyenson.music_19.BasePresenter;
import com.framgia.nguyenson.music_19.BaseView;
import com.framgia.nguyenson.music_19.data.model.Song;

public class MusicContract {
    interface View extends BaseView {
        void showDowload();

        void hideDowload();
    }

    interface Presenter extends BasePresenter<View> {
        void checkDowload(Song song, String code);
    }
}
