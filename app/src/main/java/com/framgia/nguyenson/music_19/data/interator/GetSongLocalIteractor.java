package com.framgia.nguyenson.music_19.data.interator;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.framgia.nguyenson.music_19.data.model.Song;
import com.framgia.nguyenson.music_19.data.source.SongDataSource;
import com.framgia.nguyenson.music_19.utils.MediaState;

import java.util.ArrayList;
import java.util.List;

public class GetSongLocalIteractor {
    private Context mContext;

    public GetSongLocalIteractor(Context context) {
        mContext = context;
    }

    public void getAllSong(SongDataSource.OnGetDataListener<Song> dataListener) {
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        String[] projection = {MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media._ID};
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = mContext.getContentResolver().query(
                uri, projection, selection, null, sortOrder, null);
        getSongFromCursor(cursor, dataListener, projection);
    }

    public void getSongFromCursor(Cursor cursor, SongDataSource.OnGetDataListener<Song> listener
            , String[] projection) {
        if (cursor == null) {
            listener.onGetDataError(null);
            return;
        }
        List<Song> listSong = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song.SongBuilder()
                        .uri(cursor.getString(cursor.getColumnIndexOrThrow(projection[0])))
                        .title(cursor.getString(cursor.getColumnIndexOrThrow(projection[1])))
                        .artist(cursor.getString(cursor.getColumnIndex(projection[2])))
                        .duration(cursor.getInt(cursor.getColumnIndexOrThrow(projection[3])))
                        .id(cursor.getInt(cursor.getColumnIndex(projection[4])))
                        .build();
                listSong.add(song);
            } while (cursor.moveToNext());
        }
        listener.onGetDataSuccess(listSong);
    }
}
