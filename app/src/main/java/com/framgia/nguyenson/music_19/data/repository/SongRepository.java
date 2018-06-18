package com.framgia.nguyenson.music_19.data.repository;

import android.util.Log;

import com.framgia.nguyenson.music_19.data.model.Song;
import com.framgia.nguyenson.music_19.data.source.SongDataSource;

public class SongRepository implements SongDataSource.RemoteDataSource,
        SongDataSource.LocalDataSource {
    private static SongRepository sSongRepository;
    private SongDataSource.RemoteDataSource mRemoteDataSource;
    private SongDataSource.LocalDataSource mLocalDataSource;

    private SongRepository(SongDataSource.RemoteDataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
    }

    private SongRepository(SongDataSource.LocalDataSource localDataSource) {
        mLocalDataSource = localDataSource;
    }

    public void setRemoteDataSource(SongDataSource.RemoteDataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
    }

    public void setLocalDataSource(SongDataSource.LocalDataSource localDataSource) {
        mLocalDataSource = localDataSource;
    }

    public static SongRepository getInstance(SongDataSource.RemoteDataSource remoteDataSource) {
        if (sSongRepository == null) {
            sSongRepository = new SongRepository(remoteDataSource);
        } else sSongRepository.setRemoteDataSource(remoteDataSource);
        return sSongRepository;
    }

    public static SongRepository getInstance(SongDataSource.LocalDataSource localDataSource) {

        if (sSongRepository == null) sSongRepository = new SongRepository(localDataSource);
        else sSongRepository.setLocalDataSource(localDataSource);
        return sSongRepository;
    }

    @Override
    public void getSongs(String genre, int limit, int offset,
                         SongDataSource.OnFetchDataListener<Song> listener) {
        if (mRemoteDataSource == null) return;
        mRemoteDataSource.getSongs(genre, limit, offset, listener);
    }

    @Override
    public void getSongs(SongDataSource.OnGetDataListener<Song> listener) {
        if (mLocalDataSource == null) return;
        mLocalDataSource.getSongs(listener);
    }
}
