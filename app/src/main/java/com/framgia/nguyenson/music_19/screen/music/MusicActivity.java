package com.framgia.nguyenson.music_19.screen.music;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.framgia.nguyenson.music_19.R;
import com.framgia.nguyenson.music_19.data.model.Song;
import com.framgia.nguyenson.music_19.screen.main.MainActivity;
import com.framgia.nguyenson.music_19.service.MusicService;
import com.framgia.nguyenson.music_19.utils.Constants;
import com.framgia.nguyenson.music_19.utils.MediaState;
import com.framgia.nguyenson.music_19.utils.Utils;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.framgia.nguyenson.music_19.utils.Constants.EXTRA_STATE;
import static com.framgia.nguyenson.music_19.utils.Constants.PREF_PLAY;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, MusicService.OnListener, SongAdapter.ItemClickListener,
        MusicContract.View {
    private MusicService mMusicService;
    private boolean mBound = false;
    private ImageView mImageBack;
    private TextView mTextTitle;
    private CircleImageView mCircleImageSong;
    private ImageView mImageDowload, mImageFavourite, mImageListSong;
    private TextView mTextTimeStart, mTextTimeEnd;
    private SeekBar mSeekBarSong;
    private ImageView mImageShuffleSong, mImagePreviousSong, mImagePlaySong, mImageNextSong,
            mImageRepeatSong;
    private boolean mCheckShuffle;
    private boolean mCheckRepeat;
    private boolean mCheckPlay;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Runnable mRunnable;
    private Handler mHandler;
    private int mProgress;
    private BottomSheetBehavior mBottomSheetBehavior;
    private SongAdapter mSongAdapter;
    private RecyclerView mRecyclerView;
    private MusicPresenter mPresenter;

    public static Intent getInstance(Context context) {
        Intent intent = new Intent(context, MusicActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        initViews();
        setListener();
        initPresenter();
        initSetingMusic();
        mBottomSheetBehavior = BottomSheetBehavior.from(initViewBottomSheet());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MyBinder binder = (MusicService.MyBinder) service;
            mMusicService = binder.getService();
            mBound = true;
            initData(mMusicService.getSong());
            loadImageSong(getUriSong());
            updateTime();
            mSongAdapter = new SongAdapter(mMusicService.getSongs());
            mSongAdapter.setItemClickListener(MusicActivity.this);
            mRecyclerView.setAdapter(mSongAdapter);
            mMusicService.setOnListener(MusicActivity.this);
            if (mMusicService.getStateMedia() == MediaState.PLAYING)
                mImagePlaySong.setImageResource(R.drawable.ic_pause_song);
            else mImagePlaySong.setImageResource(R.drawable.ic_play_song);
            mPresenter.checkDowload(mMusicService.getSong(), mMusicService.getCode());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    private String getUriSong() {
        String link = mMusicService.getSong().getArtworkUrl();
        if (link == null) link = mMusicService.getSong().getAvatarUrl();
        return link;
    }

    private void initPresenter() {
        mPresenter = new MusicPresenter();
        mPresenter.setView(this);
    }

    private String getUriSong(Song song) {
        String link = song.getArtworkUrl();
        if (link == null) link = mMusicService.getSong().getAvatarUrl();
        return link;
    }

    private void initViews() {
        mImageBack = findViewById(R.id.image_back);
        mTextTitle = findViewById(R.id.text_title);
        mCircleImageSong = findViewById(R.id.circle_image_song);
        mImageDowload = findViewById(R.id.image_download);
        mImageFavourite = findViewById(R.id.image_favourite);
        mImageListSong = findViewById(R.id.image_list_song);
        mTextTimeStart = findViewById(R.id.text_time_start);
        mTextTimeEnd = findViewById(R.id.text_time_end);
        mSeekBarSong = findViewById(R.id.see_song);
        mImageShuffleSong = findViewById(R.id.image_shuffle_music);
        mImagePreviousSong = findViewById(R.id.image_previous_music);
        mImagePlaySong = findViewById(R.id.image_play_music);
        mImageNextSong = findViewById(R.id.image_next_music);
        mImageRepeatSong = findViewById(R.id.image_repeat);
        mRecyclerView = findViewById(R.id.recycler_music);
    }

    private void setListener() {
        mImageBack.setOnClickListener(this);
        mImageDowload.setOnClickListener(this);
        mImageFavourite.setOnClickListener(this);
        mImageListSong.setOnClickListener(this);
        mSeekBarSong.setOnSeekBarChangeListener(this);
        mImageShuffleSong.setOnClickListener(this);
        mImagePreviousSong.setOnClickListener(this);
        mImagePlaySong.setOnClickListener(this);
        mImageNextSong.setOnClickListener(this);
        mImageRepeatSong.setOnClickListener(this);
    }

    private void initData(Song song) {
        if (song == null) return;
        mTextTitle.setText(song.getTitle());
        mTextTimeEnd.setText(Utils.
                milliSecondsToTimer(song.getDuration()));
        mSeekBarSong.setMax(song.getDuration());
    }

    private void initSetingMusic() {
        mSharedPreferences = this.getSharedPreferences(getString(R.string.setting),
                MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mCheckShuffle = mSharedPreferences.getBoolean(Constants.PREF_SHUFFLE, false);
        mCheckRepeat = mSharedPreferences.getBoolean(Constants.PREF_REPEAT, false);
        mCheckPlay = mSharedPreferences.getBoolean(Constants.PREF_PLAY, true);
        if (mCheckShuffle) mImageShuffleSong.setImageResource(R.drawable.ic_shuffle);
        if (mCheckRepeat) mImageRepeatSong.setImageResource(R.drawable.ic_repeat);
        if (mCheckPlay) mImagePlaySong.setImageResource(R.drawable.ic_pause_song);
        else mImagePlaySong.setImageResource(R.drawable.ic_play_song);
    }

    private void updateTime() {
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mSeekBarSong.setProgress(mMusicService.getCurrentPosition());
                mTextTimeStart.setText(Utils.
                        milliSecondsToTimer(mMusicService.getCurrentPosition()));
                mHandler.postDelayed(mRunnable, Constants.TIME_DELAY);
            }
        };
        mHandler.post(mRunnable);
    }

    private void loadImageSong(String uri) {
        String code = mMusicService.getCode();
        if (code.equals(Constants.MUSIC_ONLINE)) {
            Glide.with(this)
                    .load(uri)
                    .error(R.drawable.image_default_music)
                    .into(mCircleImageSong);
        } else {
            android.media.MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(mMusicService.getSong().getUri());
            byte[] data = retriever.getEmbeddedPicture();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            mCircleImageSong.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_back:
                mHandler.removeCallbacks(mRunnable);
                Intent i = new Intent(this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                break;
            case R.id.image_download:
                mPresenter.dowloadSong(mMusicService.getSong(), this);
                break;
            case R.id.image_favourite:
                break;
            case R.id.image_list_song:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.image_shuffle_music:
                shuffleSong();
                break;
            case R.id.image_previous_music:
                previousSong();
                break;
            case R.id.image_play_music:
                if (mMusicService.getStateMedia() == MediaState.PAUSE) {
                    mCheckPlay = true;
                    mEditor.putBoolean(Constants.PREF_PLAY, mCheckPlay);
                    mEditor.apply();
                    mMusicService.playSong();
                    mImagePlaySong.setImageResource(R.drawable.ic_pause_song);
                } else if (mMusicService.getStateMedia() == MediaState.PLAYING) {
                    mCheckPlay = false;
                    mEditor.putBoolean(Constants.PREF_PLAY, mCheckPlay);
                    mEditor.apply();
                    mMusicService.pause();
                    mImagePlaySong.setImageResource(R.drawable.ic_play_song);
                }
                break;
            case R.id.image_next_music:
                nextSong();
                break;
            case R.id.image_repeat:
                repeatSong();
                break;
        }
    }

    private void nextSong() {
        mMusicService.next();
        mImagePlaySong.setImageResource(R.drawable.ic_pause_song);
    }

    private void previousSong() {
        mMusicService.previous();
        mImagePlaySong.setImageResource(R.drawable.ic_pause_song);
    }

    private void shuffleSong() {
        if (mCheckRepeat) {
            mCheckRepeat = false;
            mImageRepeatSong.setImageResource(R.drawable.ic_repeat_white);
            mEditor.putBoolean(Constants.PREF_REPEAT, mCheckRepeat);
        }
        if (mCheckShuffle) {
            mCheckShuffle = false;
            mImageShuffleSong.setImageResource(R.drawable.ic_shuffle_white);
            mEditor.putBoolean(Constants.PREF_SHUFFLE, mCheckShuffle);
        } else {
            mCheckShuffle = true;
            mImageShuffleSong.setImageResource(R.drawable.ic_shuffle);
            mEditor.putBoolean(Constants.PREF_SHUFFLE, mCheckShuffle);
        }
        mEditor.apply();
    }

    private void repeatSong() {
        if (mCheckShuffle) {
            mCheckShuffle = false;
            mImageShuffleSong.setImageResource(R.drawable.ic_shuffle_white);
            mEditor.putBoolean(Constants.PREF_SHUFFLE, mCheckShuffle);
        }
        if (mCheckRepeat) {
            mCheckRepeat = false;
            mEditor.putBoolean(Constants.PREF_REPEAT, mCheckRepeat);
            mImageRepeatSong.setImageResource(R.drawable.ic_repeat_white);
            mMusicService.getMediaPlayer().setLooping(mCheckRepeat);
        } else {
            mCheckRepeat = true;
            mImageRepeatSong.setImageResource(R.drawable.ic_repeat);
            mEditor.putBoolean(Constants.PREF_REPEAT, mCheckRepeat);
            mMusicService.getMediaPlayer().setLooping(mCheckRepeat);
        }
        mEditor.apply();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mMusicService.getMediaPlayer() != null && fromUser) {
            mSeekBarSong.setProgress(progress);
            mMusicService.getMediaPlayer().seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    public void onListenerSong(Song song) {
        initData(song);
        loadImageSong(getUriSong());
        updateTime();
        mSongAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateStatePlay(boolean state) {
        if (state) mImagePlaySong.setImageResource(R.drawable.ic_pause_song);
        else mImagePlaySong.setImageResource(R.drawable.ic_play_song);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }

    private NestedScrollView initViewBottomSheet() {
        NestedScrollView root = findViewById(R.id.bottom_sheet);
        return root;
    }

    @Override
    public void onCLickItem(int position) {
        if (mMusicService.getSong().getId() == mMusicService.getSongs().get(position).getId())
            return;
        startService(MusicService.getInstance(this, position, mMusicService.getSongs(), mMusicService.getCode()));
        mTextTitle.setText(mMusicService.getSongs().get(position).getTitle());
        loadImageSong(getUriSong(mMusicService.getSongs().get(position)));
    }

    @Override
    public void showDowload() {
        mImageDowload.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDowload() {
        mImageDowload.setVisibility(View.INVISIBLE);
    }
}
