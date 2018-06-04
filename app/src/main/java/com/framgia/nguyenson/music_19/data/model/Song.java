package com.framgia.nguyenson.music_19.data.model;

public class Song {
    private int mId;
    private String mArtworkUrl;
    private String mDowloadUrl;
    private String mDuration;
    private String mArtist;
    private String mAvatarUrl;

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        mAvatarUrl = avatarUrl;
    }

    private String mTitle;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getArtworkUrl() {
        return mArtworkUrl;
    }

    public void setArtworkUrl(String artworkUrl) {
        mArtworkUrl = artworkUrl;
    }

    public String getDowloadUrl() {
        return mDowloadUrl;
    }

    public void setDowloadUrl(String dowloadUrl) {
        mDowloadUrl = dowloadUrl;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        mDuration = duration;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        mArtist = artist;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public static class SongBuilder {
        private Song mSong;

        public SongBuilder() {
            mSong = new Song();
        }

        public SongBuilder id(int id) {
            mSong.setId(id);
            return this;
        }

        public SongBuilder artworkUrl(String artworkUrl) {
            mSong.setArtworkUrl(artworkUrl);
            return this;
        }

        public SongBuilder dowloadUrl(String url) {
            mSong.setDowloadUrl(url);
            return this;
        }

        public SongBuilder duration(String duration) {
            mSong.setDuration(duration);
            return this;
        }

        public SongBuilder artist(String artist) {
            mSong.setArtist(artist);
            return this;
        }

        public SongBuilder title(String title) {
            mSong.setTitle(title);
            return this;
        }

        public SongBuilder avatar(String avatarUrl) {
            mSong.setAvatarUrl(avatarUrl);
            return this;
        }

        public Song build() {
            return mSong;
        }
    }
}
