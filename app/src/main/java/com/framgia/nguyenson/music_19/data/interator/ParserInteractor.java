package com.framgia.nguyenson.music_19.data.interator;

import com.framgia.nguyenson.music_19.data.model.Song;
import com.framgia.nguyenson.music_19.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ParserInteractor {

    public String getContentFromUrl(String strUrl) throws IOException {
        String content = "";
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        url = new URL(strUrl);
        httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod(Constants.METHOD_GET);
        httpURLConnection.setConnectTimeout(Constants.CONNECTION_TIME_OUT);
        httpURLConnection.setReadTimeout(Constants.READ_INPUT_TIME_OUT);
        httpURLConnection.setDoInput(true);
        httpURLConnection.connect();
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            content = parserResultFromContent(httpURLConnection.getInputStream());
        }
        return content;
    }

    public String parserResultFromContent(InputStream is) throws IOException {
        String result = "";
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Constants.CHARSET_NAME_UTF8));
        String line = "";
        while ((line = reader.readLine()) != null) {
            result += line;
        }
        is.close();
        return result;
    }

    public List<Song> parserListSong(String result) {
        List<Song> songList = new ArrayList<>();
        try {
            JSONObject genre = new JSONObject(result);
            JSONArray collection = genre.getJSONArray("collection");
            for (int i = 0; i < collection.length(); i++) {
                JSONObject object = (JSONObject) collection.get(i);
                JSONObject track = object.getJSONObject("track");
                JSONObject user = track.getJSONObject("user");
                Song song = new Song.SongBuilder()
                        .id(track.optInt("id", 0))
                        .artworkUrl(track.optString("artwork_url", null))
                        .dowloadUrl(track.optString("download_url", null))
                        .duration(track.optString("duration", null))
                        .avatar(user.optString("user",null))
                        .title(track.optString("title", null))
                        .build();
                songList.add(song);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return songList;
    }
}
