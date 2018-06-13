package com.framgia.nguyenson.music_19.utils;

import com.framgia.nguyenson.music_19.BuildConfig;

public class Utils {
    public static String createUrlContent(String genre, int limit, int offset) {
        return String.format("%s%s%s&%s=%s&%s=%d&%s=%d", Constants.URL_BASE,
                Constants.CONTENT_URL, genre, Constants.CLIENT_ID,
                BuildConfig.API_KEY, Constants.LIMIT, limit, Constants.OFFSET, offset);
    }

    public static String createUrlStreamMusic(String uri) {
        return String.format("%s%s=%s", uri, Constants.STREAM, BuildConfig.API_KEY);
    }

    public static class Api {
        public static final String COLLECTION = "collection";
        public static final String TRACK = "track";
        public static final String USER = "user";
        public static final String ID = "id";
        public static final String ARTWORK_URL = "artwork_url";
        public static final String DOWLOAD_URL = "download_url";
        public static final String DURATION = "duration";
        public static final String TITLE = "title";
        public static final String URI = "uri";
        public static final String POSITION = "position";
        public static final String KEY_LIST = "key_list";
        public static final String BUNDLE = "bundle";
    }

    public static String milliSecondsToTimer(long milliseconds) {
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("%02d:%02d", minutes, seconds);
    }
}
