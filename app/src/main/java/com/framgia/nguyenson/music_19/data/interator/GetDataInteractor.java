package com.framgia.nguyenson.music_19.data.interator;

import android.os.AsyncTask;

import com.framgia.nguyenson.music_19.data.model.Song;
import com.framgia.nguyenson.music_19.data.source.SongDataSource;
import com.framgia.nguyenson.music_19.utils.Constants;

import java.io.IOException;
import java.util.List;

public class GetDataInteractor {
    protected SongDataSource.OnFetchDataListener mListener;

    public GetDataInteractor(SongDataSource.OnFetchDataListener<Song> listener) {
        mListener = listener;
    }

    public void loadData(String url) {
        new FetchData().execute(url);
    }

    public class FetchData extends AsyncTask<String, Void, List<Song>> {

        public FetchData() {
        }

        @Override
        protected List<Song> doInBackground(String... strings) {
            ParserInteractor parserInteractor = new ParserInteractor();
            String result = "";
            try {
                result = parserInteractor.getContentFromUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return  null;
            }
            return parserInteractor.parserListSong(result);
        }

        @Override
        protected void onPostExecute(List<Song> songs) {
            super.onPostExecute(songs);
            if(songs != null){
                mListener.onFetchDataSuccess(songs);
            }else
                mListener.onFetchDataError(Constants.ERROR_NO_DATA);
        }
    }
}
