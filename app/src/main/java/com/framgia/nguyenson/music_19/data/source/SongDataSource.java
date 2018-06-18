package com.framgia.nguyenson.music_19.data.source;

import com.framgia.nguyenson.music_19.data.model.Song;

import java.util.List;

public interface SongDataSource {

    interface RemoteDataSource {
        void getSongs(String genre, int limit, int offset,
                      OnFetchDataListener<Song> listener);
    }

    interface OnFetchDataListener<T> {
        void onFetchDataSuccess(List<T> list);

        void onFetchDataError(String message);
    }

    interface LocalDataSource {
        void getSongs(OnGetDataListener<Song> listener);
    }

    interface OnGetDataListener<T> {
        void onGetDataSuccess(List<T> list);

        void onGetDataError(String message);
    }
}
