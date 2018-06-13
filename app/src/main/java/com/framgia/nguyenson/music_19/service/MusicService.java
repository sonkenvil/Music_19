package com.framgia.nguyenson.music_19.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

import com.framgia.nguyenson.music_19.R;
import com.framgia.nguyenson.music_19.data.model.Song;
import com.framgia.nguyenson.music_19.utils.Constants;
import com.framgia.nguyenson.music_19.utils.MediaState;
import com.framgia.nguyenson.music_19.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class MusicService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {
    public static final String ACTION_PLAY = "ACTION_PLAY";
    private static final String EXTRA_LIST_SONG = "EXTRA_LIST_SONG";
    private static final String EXTRA_POSITION = "EXTRA_POSITION";
    private final IBinder mBinder = new MyBinder();
    private MediaPlayer mMediaPlayer;
    private ArrayList<Song> mSongs;
    private int mIndex = 0;
    private int mStateMedia = MediaState.IDLE;
    private SharedPreferences mSharedPreferences;
    private boolean mCheckRepeat;
    private boolean mCheckShuffle;
    private boolean mCheckPlay;
    private OnListener mOnListener;

    public static Intent getInstance(Context context, int position, List<Song> songList) {
        Intent intent = new Intent(context, MusicService.class);
        intent.setAction(ACTION_PLAY);
        intent.putParcelableArrayListExtra(EXTRA_LIST_SONG, (ArrayList<? extends Parcelable>) songList);
        intent.putExtra(EXTRA_POSITION, position);
        return intent;
    }

    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer();
        mSharedPreferences = this.getSharedPreferences(getString(R.string.setting), MODE_PRIVATE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null || intent.getAction() == null) return START_NOT_STICKY;
        switch (intent.getAction()) {
            case ACTION_PLAY:
                Bundle bundle = intent.getExtras();
                mSongs = bundle.getParcelableArrayList(EXTRA_LIST_SONG);
                int position = bundle.getInt(EXTRA_POSITION);
                if (mSongs.get(mIndex).getId() != mSongs.get(position).getId()) {
                    mStateMedia = MediaState.IDLE;
                    mIndex = position;
                }
                playSong();
                break;
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mCheckShuffle = mSharedPreferences.getBoolean(Constants.PREF_SHUFFLE, false);
        mCheckRepeat = mSharedPreferences.getBoolean(Constants.PREF_REPEAT, false);
        if (mCheckRepeat) {
            mMediaPlayer.start();
        } else if (mCheckShuffle) {
            shuffleSong();
            next();
        } else {
            next();
        }
    }


    public class MyBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public void playSong() {
        if (mStateMedia == MediaState.IDLE || mStateMedia == MediaState.COMPLETE) {
            mMediaPlayer.reset();
            try {
                mMediaPlayer.setDataSource(Utils.createUrlStreamMusic(mSongs.get(mIndex).getUri()));
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.prepareAsync();
                mMediaPlayer.setOnPreparedListener(this);
                mMediaPlayer.setOnCompletionListener(this);
                mStateMedia = MediaState.PLAYING;
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putBoolean(Constants.PREF_PLAY, true);
                editor.apply();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (mStateMedia == MediaState.PAUSE) {
            mCheckPlay = mSharedPreferences.getBoolean(Constants.PREF_PLAY, true);
            if (mCheckPlay) {
                mMediaPlayer.start();
                mStateMedia = MediaState.PLAYING;
            } else mStateMedia = MediaState.PAUSE;
        }
    }

    public void pause() {
        mMediaPlayer.pause();
        mStateMedia = MediaState.PAUSE;
    }

    public void next() {
        if (mIndex == mSongs.size() - 1) mIndex = 0;
        else mIndex++;
        mStateMedia = MediaState.IDLE;
        playSong();
        mOnListener.onListenerSong(mSongs.get(mIndex));
    }

    public void previous() {
        if (mIndex == 0) mIndex = mSongs.size() - 1;
        else mIndex--;
        mStateMedia = MediaState.IDLE;
        playSong();
        mOnListener.onListenerSong(mSongs.get(mIndex));
    }

    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    public Song getSong() {
        return mSongs.get(mIndex);
    }

    public void playCurrentTime(int currentTime) {
        mMediaPlayer.seekTo(currentTime);
    }

    public int getStateMedia() {
        return mStateMedia;
    }

    public void setOnListener(OnListener onListener) {
        mOnListener = onListener;
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public void shuffleSong() {
        Collections.shuffle(mSongs);
        for (Song i : mSongs) {
            if (mSongs.get(mIndex).getId() == i.getId()) {
                mIndex = mSongs.indexOf(i);
            }
            break;
        }
    }

    public interface OnListener {
        void onListenerSong(Song song);
    }
}
