package com.framgia.nguyenson.music_19.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.framgia.nguyenson.music_19.R;
import com.framgia.nguyenson.music_19.data.model.Song;
import com.framgia.nguyenson.music_19.screen.music.MusicActivity;
import com.framgia.nguyenson.music_19.utils.Constants;
import com.framgia.nguyenson.music_19.utils.MediaState;
import com.framgia.nguyenson.music_19.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MusicService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {
    public static final String ACTION_PLAY = "ACTION_PLAY";
    private static final String EXTRA_LIST_SONG = "EXTRA_LIST_SONG";
    private static final String EXTRA_POSITION = "EXTRA_POSITION";
    private static final String EXTRA_CODE = "EXTRA_CODE";
    private static final String ACTION_PLAY_NOTIFI = "com.framgia.music.action.ACTION_PLAY_NOTIFI";
    private static final String ACTION_PREVIOUS_NOTIFI = "com.framgia.music.action.ACTION_PREVIOUS_NOTIFI";
    private static final String ACTION_NEXT_NOTIFI = "com.framgia.music.action.ACTION_NEXT_NOTIFI";
    private static final String ACTION_CLEAR_NOTIFI = "com.framgia.music.action.ACTION.CLEAR_NOTIFI";
    private static final int REQUEST_CODE_NEXT = 0;
    private static final int REQUEST_CODE_PREVIOUS = 1;
    private static final int REQUEST_CODE_PLAY = 2;
    private static final int REQUEST_CODE_CLEAR = 3;
    private static final int ID_NOTIFICATION = 1337;
    private final IBinder mBinder = new MyBinder();
    private MediaPlayer mMediaPlayer;
    private ArrayList<Song> mSongs;
    private int mIndex = 0;
    private int mStateMedia = MediaState.IDLE;
    private SharedPreferences mSharedPreferences;
    private boolean mCheckPlay;
    private OnListener mOnListener;
    private Notification mNotification;
    private RemoteViews mRemoteViews;
    private String mCode = Constants.MUSIC_ONLINE;
    private SharedPreferences.Editor mEditor;

    public static Intent getInstance(Context context, int position, List<Song> songList,
                                     String code) {
        Intent intent = new Intent(context, MusicService.class);
        intent.setAction(ACTION_PLAY);
        intent.putParcelableArrayListExtra(EXTRA_LIST_SONG, (ArrayList<? extends Parcelable>) songList);
        intent.putExtra(EXTRA_POSITION, position);
        intent.putExtra(EXTRA_CODE, code);
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
                String code = bundle.getString(EXTRA_CODE);
                if (mCode.equals(code)) {
                    if (mSongs.get(mIndex).getId() != mSongs.get(position).getId()) {
                        mStateMedia = MediaState.IDLE;
                        mIndex = position;
                    }
                } else if (!mCode.equals(code)) {
                    mIndex = 0;
                    mMediaPlayer.reset();
                    mCode = code;
                    if (mSongs.get(mIndex).getId() != mSongs.get(position).getId()) {
                        mStateMedia = MediaState.IDLE;
                        mIndex = position;
                    }
                }
                playSong();
                break;
            case ACTION_PREVIOUS_NOTIFI:
                previous();
                break;
            case ACTION_PLAY_NOTIFI:
                mEditor = mSharedPreferences.edit();
                if (mStateMedia == MediaState.PLAYING) {
                    mCheckPlay = false;
                    mEditor.putBoolean(Constants.PREF_PLAY, mCheckPlay);
                    mEditor.apply();
                    pause();
                    mOnListener.updateStatePlay(false);
                } else if (mStateMedia == MediaState.PAUSE) {
                    mCheckPlay = true;
                    mEditor.putBoolean(Constants.PREF_PLAY, mCheckPlay);
                    mEditor.apply();
                    playSong();
                    mOnListener.updateStatePlay(true);
                }
                break;
            case ACTION_NEXT_NOTIFI:
                next();
                break;
            case ACTION_CLEAR_NOTIFI:
                stopForeground(true);
                stopSelf();
                break;
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer.start();
        showNotification(mSongs.get(mIndex).getTitle());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        boolean mCheckShuffle = mSharedPreferences.getBoolean(Constants.PREF_SHUFFLE, false);
        boolean mCheckRepeat = mSharedPreferences.getBoolean(Constants.PREF_REPEAT, false);
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
                if (mCode.equals(Constants.MUSIC_OFFLINE))
                    mMediaPlayer.setDataSource(mSongs.get(mIndex).getUri());
                else
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
        if (mRemoteViews != null) updateNotification();
    }

    public void pause() {
        mStateMedia = MediaState.PAUSE;
        mMediaPlayer.pause();
        if (mRemoteViews != null) updateNotification();
    }

    public void next() {
        if (mIndex == mSongs.size() - 1) mIndex = 0;
        else mIndex++;
        mStateMedia = MediaState.IDLE;
        if (mRemoteViews != null) updateNotification();
        playSong();
        mOnListener.onListenerSong(mSongs.get(mIndex));
        mOnListener.updateStatePlay(true);
    }

    public void previous() {
        if (mIndex == 0) mIndex = mSongs.size() - 1;
        else mIndex--;
        mStateMedia = MediaState.IDLE;
        if (mRemoteViews != null) updateNotification();
        playSong();
        mOnListener.onListenerSong(mSongs.get(mIndex));
        mOnListener.updateStatePlay(true);
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

    private void showNotification(String title) {
        mRemoteViews = new RemoteViews(getPackageName(), R.layout.notification_music);
        mRemoteViews.setTextViewText(R.id.text_notification_title, title);
        nextMusicNotifi();
        previousMusicNotifi();
        playMusicNotifi();
        clearNotifi();
        Intent intent = new Intent(this, MusicActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivities(this, (int) System.currentTimeMillis(),
                        new Intent[]{intent}, 0);
        mNotification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification_music)
                .setContentIntent(pendingIntent)
                .setContent(mRemoteViews)
                .setOnlyAlertOnce(true)
                .setOngoing(false)
                .build();
        startForeground(ID_NOTIFICATION, mNotification);
    }

    public String getCode() {
        return mCode;
    }

    private void nextMusicNotifi() {
        Intent intentActionNext = new Intent();
        intentActionNext.setAction(ACTION_NEXT_NOTIFI);
        intentActionNext.setClass(getApplicationContext(), MusicService.class);
        PendingIntent peServiceNext =
                PendingIntent.getService(getApplicationContext(), REQUEST_CODE_NEXT,
                        intentActionNext, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.image_next_notification, peServiceNext);
    }

    private void previousMusicNotifi() {
        Intent intentActionPrev = new Intent();
        intentActionPrev.setAction(ACTION_PREVIOUS_NOTIFI);
        intentActionPrev.setClass(getApplicationContext(), MusicService.class);
        PendingIntent peServicePre =
                PendingIntent.getService(getApplicationContext(), REQUEST_CODE_PREVIOUS,
                        intentActionPrev, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.image_previous_notification, peServicePre);
    }

    private void playMusicNotifi() {
        Intent intentActionPrev = new Intent();
        intentActionPrev.setAction(ACTION_PLAY_NOTIFI);
        intentActionPrev.setClass(getApplicationContext(), MusicService.class);
        PendingIntent peServicePre =
                PendingIntent.getService(getApplicationContext(), REQUEST_CODE_PLAY,
                        intentActionPrev, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.image_play_notification, peServicePre);
    }

    private void clearNotifi() {
        Intent intentActionClear = new Intent();
        intentActionClear.setAction(ACTION_CLEAR_NOTIFI);
        intentActionClear.setClass(this, MusicService.class);
        PendingIntent peServiceClear =
                PendingIntent.getService(this, REQUEST_CODE_CLEAR, intentActionClear,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.image_clear, peServiceClear);
    }

    private void updateNotification() {
        mRemoteViews.setTextViewText(R.id.text_notification_title, mSongs.get(mIndex).getTitle());
        mRemoteViews.setImageViewResource(R.id.image_play_notification, R.drawable.ic_pause_song);
        if (mStateMedia == MediaState.PAUSE) {
            mRemoteViews.setViewVisibility(R.id.image_clear, View.VISIBLE);
            mRemoteViews.setImageViewResource(R.id.image_play_notification, R.drawable.ic_play_song);
        } else {
            mRemoteViews.setViewVisibility(R.id.image_clear, View.INVISIBLE);
            mRemoteViews.setImageViewResource(R.id.image_play_notification, R.drawable.ic_pause_song);
        }
        startForeground(ID_NOTIFICATION, mNotification);
    }


    public interface OnListener {
        void onListenerSong(Song song);

        void updateStatePlay(boolean state);
    }

    public ArrayList<Song> getSongs() {
        return mSongs;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.stop();
        mMediaPlayer.release();
    }
}
