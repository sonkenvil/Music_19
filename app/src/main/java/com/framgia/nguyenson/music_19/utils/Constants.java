package com.framgia.nguyenson.music_19.utils;

public class Constants {
    public static class GenreBase {
        public static final String URL_BASE = "https://api-v2.soundcloud.com";
        public static final String CONTENT_URL = "/charts?kind=top&genre=soundcloud%3Agenres%3A";
        public static final String CLIENT_ID = "client_id";
        public static final String LIMIT = "limit";
        public static final String OFFSET = "offset";
        public static final int DEFAULT_LIMIT = 20;
        public static final int DEFAULT_OFFSET = 20;
        public static final String STREAM = "/stream?client_id";
        public static final String DOWLOAD = "?client_id=";
        public static final String GENRE_ALL_MUSIC = "all-music";
        public static final String GENRE_ALL_AUDIO = "all-audio";
        public static final String GENRE_ALTERNATIVEROCK = "alternativerock";
        public static final String GENRE_AMBIENT = "ambient";
        public static final String GENRE_CLASSICAL = "classical";
        public static final String GENRE_COUNTRY = "country";
        public static final String[] GENRE = {GENRE_ALL_MUSIC, GENRE_ALL_AUDIO, GENRE_ALTERNATIVEROCK,
                GENRE_AMBIENT, GENRE_CLASSICAL, GENRE_COUNTRY};
    }

    public static final int TIME_DELAY = 1000;
    public static final String CHARSET_NAME_UTF8 = "UTF-8";
    public static final String METHOD_GET = "GET";
    public static final String ERROR_NO_DATA = "No Data";
    public static final int CONNECTION_TIME_OUT = 5000;
    public static final int READ_INPUT_TIME_OUT = 5000;
    public static final String PREF_REPEAT = "PREF_REPEAT";
    public static final String PREF_SHUFFLE = "PREF_SHUFFLE";
    public static final String PREF_PLAY = "PREF_PLAY";
    public static final String EXTRA_STATE = "EXTRA_STATE";
    public static final String MUSIC_ONLINE = "MUSIC_ONLINE";
    public static final String MUSIC_OFFLINE = "MUSIC_OFFLINE";

    public Constants() {
    }
}
