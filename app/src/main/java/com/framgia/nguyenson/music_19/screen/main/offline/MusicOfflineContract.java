package com.framgia.nguyenson.music_19.screen.main.offline;

import com.framgia.nguyenson.music_19.BasePresenter;
import com.framgia.nguyenson.music_19.BaseView;
import com.framgia.nguyenson.music_19.data.model.Song;


import java.util.List;

public class MusicOfflineContract {
    interface View extends BaseView {

        void showData(List<Song> list);

        void showError(String message);

    }

    interface Presenter extends BasePresenter<MusicOfflineContract.View> {
        void getAllMusic();
    }
}
