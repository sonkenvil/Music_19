package com.framgia.nguyenson.music_19.screen.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.framgia.nguyenson.music_19.R;
import com.framgia.nguyenson.music_19.data.model.Song;
import com.framgia.nguyenson.music_19.screen.main.offline.MusicOfflineFragment;
import com.framgia.nguyenson.music_19.screen.main.online.MusicOnlineFragment;
import com.framgia.nguyenson.music_19.screen.music.MusicActivity;
import com.framgia.nguyenson.music_19.service.MusicService;
import com.framgia.nguyenson.music_19.utils.Constants;
import com.framgia.nguyenson.music_19.utils.MediaState;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        TextView.OnEditorActionListener, NavigationView.OnNavigationItemSelectedListener,
        MusicService.OnListener {
    private ImageView mImageMenu, mImageSearch;
    private EditText mEditSeachMusic;
    private FragmentManager mFragmentManager;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private ImageView mImageMusic, mImagePrevious, mImagePlay, mImageNext;
    private TextView mTextTitle;
    private MusicService mMusicService;
    private boolean mBound;
    private ConstraintLayout mConstraintControl;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private boolean mCheckPlay;
    private static final String MUSIC_ONLINE = "MUSIC_ONLINE";
    private static final String MUSIC_OFFLINE = "MUSIC_OFFLINE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setOnListener();
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        MusicOnlineFragment musicOnlineFragment = new MusicOnlineFragment();
        transaction.add(R.id.content_frame, musicOnlineFragment, MUSIC_ONLINE);
        transaction.commit();
        initSetting();
        mTextTitle.setSelected(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void setOnListener() {
        mImageMenu.setOnClickListener(this);
        mImageSearch.setOnClickListener(this);
        mEditSeachMusic.setOnEditorActionListener(this);
        mNavigationView.setNavigationItemSelectedListener(this);
        mImagePlay.setOnClickListener(this);
        mImagePrevious.setOnClickListener(this);
        mImageNext.setOnClickListener(this);
    }

    private void initViews() {
        mImageMenu = findViewById(R.id.image_menu);
        mImageSearch = findViewById(R.id.image_search);
        mEditSeachMusic = findViewById(R.id.edit_search_music);
        mNavigationView = findViewById(R.id.nav_view);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mImageMusic = findViewById(R.id.image_song_main);
        mImagePlay = findViewById(R.id.image_play_main);
        mImageNext = findViewById(R.id.image_next_main);
        mImagePrevious = findViewById(R.id.image_previous_main);
        mTextTitle = findViewById(R.id.text_title_main);
        mConstraintControl = findViewById(R.id.constraint_music);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MyBinder binder = (MusicService.MyBinder) service;
            mMusicService = binder.getService();
            mBound = true;
            mMusicService.setOnListener(MainActivity.this);
            if (mMusicService.getStateMedia() == MediaState.PLAYING ||
                    mMusicService.getStateMedia() == MediaState.PAUSE) {
                mConstraintControl.setVisibility(View.VISIBLE);
                mTextTitle.setText(mMusicService.getSong().getTitle());
                loadImageMusic(getUriSong());
            }
            boolean state = mSharedPreferences.getBoolean(Constants.PREF_PLAY, false);
            if (state) mImagePlay.setImageResource(R.drawable.ic_pause_song);
            else mImagePlay.setImageResource(R.drawable.ic_play_song);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.image_menu:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.image_search:
                break;
            case R.id.image_previous_main:
                previousSong();
                break;
            case R.id.image_play_main:
                if (mMusicService.getStateMedia() == MediaState.PAUSE) {
                    mCheckPlay = true;
                    mEditor.putBoolean(Constants.PREF_PLAY, mCheckPlay);
                    mEditor.apply();
                    mMusicService.playSong();
                    mImagePlay.setImageResource(R.drawable.ic_pause_song);
                } else if (mMusicService.getStateMedia() == MediaState.PLAYING) {
                    mCheckPlay = false;
                    mEditor.putBoolean(Constants.PREF_PLAY, mCheckPlay);
                    mEditor.apply();
                    mMusicService.pause();
                    mImagePlay.setImageResource(R.drawable.ic_play_song);
                }
                break;
            case R.id.image_next_main:
                nextSong();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    private void initSetting() {
        mSharedPreferences = this.getSharedPreferences(getString(R.string.setting),
                MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mCheckPlay = mSharedPreferences.getBoolean(Constants.PREF_PLAY, true);
        if (mCheckPlay) mImagePlay.setImageResource(R.drawable.ic_pause_song);
        else mImagePlay.setImageResource(R.drawable.ic_play_song);
    }

    private void previousSong() {
        mMusicService.previous();
        mImagePlay.setImageResource(R.drawable.ic_pause_song);
    }

    private void nextSong() {
        mMusicService.next();
        mImagePlay.setImageResource(R.drawable.ic_pause_song);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        item.setChecked(true);
        switch (id) {
            case R.id.nav_music_online:
                Fragment fragmentMusicOnline = mFragmentManager.findFragmentByTag(MUSIC_ONLINE);
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                if (fragmentMusicOnline != null) {
                    transaction.replace(R.id.content_frame, fragmentMusicOnline);
                    transaction.commit();
                } else {
                    MusicOnlineFragment onlineFragment = new MusicOnlineFragment();
                    transaction.replace(R.id.content_frame, onlineFragment, MUSIC_ONLINE);
                    transaction.commit();
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_offline:
                Fragment fragmentMusicOffline = mFragmentManager.findFragmentByTag(MUSIC_OFFLINE);
                FragmentTransaction transactionOff = mFragmentManager.beginTransaction();
                if (fragmentMusicOffline != null) {
                    transactionOff.replace(R.id.content_frame, fragmentMusicOffline);
                    transactionOff.commit();
                } else {
                    MusicOfflineFragment offlineFragment = new MusicOfflineFragment();
                    transactionOff.replace(R.id.content_frame, offlineFragment, MUSIC_OFFLINE);
                    transactionOff.commit();
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_favourite:
                break;
        }
        return false;
    }

    private void loadImageMusic(String uri) {
        String code = mMusicService.getCode();
        if (code.equals(Constants.MUSIC_ONLINE)) {
            Glide.with(this)
                    .load(uri)
                    .error(R.drawable.image_default_music)
                    .into(mImageMusic);
        } else {
            android.media.MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(mMusicService.getSong().getUri());
            byte[] data = retriever.getEmbeddedPicture();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            mImageMusic.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onListenerSong(Song song) {
        mTextTitle.setText(song.getTitle());
        loadImageMusic(getUriSong());
    }

    private String getUriSong() {
        String link = mMusicService.getSong().getArtworkUrl();
        if (link == null) link = mMusicService.getSong().getAvatarUrl();
        return link;
    }

    @Override
    public void updateStatePlay(boolean state) {
        if (state) mImagePlay.setImageResource(R.drawable.ic_pause_song);
        else mImagePlay.setImageResource(R.drawable.ic_play_song);
    }
}
