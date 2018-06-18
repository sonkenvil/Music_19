package com.framgia.nguyenson.music_19.screen.music;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.framgia.nguyenson.music_19.BuildConfig;
import com.framgia.nguyenson.music_19.data.model.Song;
import com.framgia.nguyenson.music_19.utils.Constants;

import static android.content.Context.DOWNLOAD_SERVICE;

public class MusicPresenter implements MusicContract.Presenter {
    private MusicContract.View mView;

    @Override
    public void checkDowload(Song song, String code) {
        if (song == null) return;
        if (code.equals(Constants.MUSIC_ONLINE)) {
            if (song.getDowloadUrl().equals("null"))
                mView.hideDowload();
            else
                mView.showDowload();
        }
    }

    public void dowloadSong(Song song, Context context) {
        if (song == null) return;
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(
                DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(song.getDowloadUrl() + Constants.GenreBase.DOWLOAD + BuildConfig.API_KEY));
        Log.d("TAG", "" + song.getDowloadUrl() +
                Constants.GenreBase.DOWLOAD + BuildConfig.API_KEY);
        request.setTitle("Download Song");
        request.setDescription(song.getTitle());
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                song.getTitle() + ".mp3");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        downloadManager.enqueue(request);
    }

    @Override
    public void setView(MusicContract.View view) {
        mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
