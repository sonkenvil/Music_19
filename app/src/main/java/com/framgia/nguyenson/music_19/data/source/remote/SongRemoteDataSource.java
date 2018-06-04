package com.framgia.nguyenson.music_19.data.source.remote;

import com.framgia.nguyenson.music_19.data.interator.GetDataInteractor;
import com.framgia.nguyenson.music_19.data.model.Song;
import com.framgia.nguyenson.music_19.data.source.SongDataSource;
import com.framgia.nguyenson.music_19.utils.Constants;
import com.framgia.nguyenson.music_19.utils.Utils;

public class SongRemoteDataSource implements SongDataSource.RemoteDataSource {
    private static SongRemoteDataSource mSongRemoteDataSource;

    public SongRemoteDataSource() {
    }

    public static SongRemoteDataSource getInstance() {
        if (mSongRemoteDataSource == null) {
            mSongRemoteDataSource = new SongRemoteDataSource();
        }
        return mSongRemoteDataSource;
    }

    @Override
    public void getSongs(int position, int limit, int offset, SongDataSource.OnFetchDataListener<Song> listener) {
        GetDataInteractor getDataInteractor = new GetDataInteractor(listener);
        String url = Utils.createUrlContent(Constants.GENRE[position], limit, offset);
        getDataInteractor.loadData(url);
    }
}
