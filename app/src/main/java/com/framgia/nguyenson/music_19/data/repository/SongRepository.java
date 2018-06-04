package com.framgia.nguyenson.music_19.data.repository;

import com.framgia.nguyenson.music_19.data.model.Song;
import com.framgia.nguyenson.music_19.data.source.SongDataSource;
import com.framgia.nguyenson.music_19.data.source.remote.SongRemoteDataSource;

public class SongRepository implements SongDataSource.RemoteDataSource {
    private static SongRepository sSongRepository;
    private SongDataSource.RemoteDataSource mRemoteDataSource;

    private SongRepository(SongDataSource.RemoteDataSource remote) {
        mRemoteDataSource = remote;
    }

    public static SongRepository getInstance() {
        if (sSongRepository == null) {
            sSongRepository = new SongRepository(SongRemoteDataSource.getInstance());
        }
        return sSongRepository;
    }

    @Override
    public void getSongs(int position, int limit, int offset,
                         SongDataSource.OnFetchDataListener<Song> listener) {
        if (mRemoteDataSource == null) return;
        mRemoteDataSource.getSongs(position, limit, offset, listener);
    }
}
