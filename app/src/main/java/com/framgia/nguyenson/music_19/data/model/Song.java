package com.framgia.nguyenson.music_19.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {
    private int mId;
    private String mArtworkUrl;
    private String mDowloadUrl;
    private int mDuration;
    private String mArtist;
    private String mAvatarUrl;
    private String mUri;

    public Song() {
    }

    protected Song(Parcel in) {
        mId = in.readInt();
        mArtworkUrl = in.readString();
        mDowloadUrl = in.readString();
        mDuration = in.readInt();
        mArtist = in.readString();
        mAvatarUrl = in.readString();
        mTitle = in.readString();
        mUri = in.readString();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

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

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
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

    public String getUri() {
        return mUri;
    }

    public void setUri(String uri) {
        mUri = uri;
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mArtworkUrl);
        dest.writeString(mDowloadUrl);
        dest.writeInt(mDuration);
        dest.writeString(mArtist);
        dest.writeString(mAvatarUrl);
        dest.writeString(mTitle);
        dest.writeString(mUri);
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

        public SongBuilder duration(int duration) {
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

        public SongBuilder uri(String uri) {
            mSong.setUri(uri);
            return this;
        }

        public Song build() {
            return mSong;
        }
    }
}
