package com.framgia.nguyenson.music_19.data.source;

import com.framgia.nguyenson.music_19.data.model.Song;

import java.util.List;

public interface SongDataSource {

    interface RemoteDataSource {
        void getSongs(int position, OnFetchDataListener<Song> listener);
    }

    interface OnFetchDataListener<T> {
        void onFetchDataSuccess(int position, List<T> list);

        void onFetchDataError(String message);
    }
}
