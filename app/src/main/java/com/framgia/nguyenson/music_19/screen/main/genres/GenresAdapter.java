package com.framgia.nguyenson.music_19.screen.main.genres;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.framgia.nguyenson.music_19.R;
import com.framgia.nguyenson.music_19.data.model.Song;

import java.util.ArrayList;
import java.util.List;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.ViewHolder> {
    private List<Song> mListSong;

    public GenresAdapter(ArrayList<Song> list) {
        mListSong = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_music, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(mListSong.get(position));
    }

    public void setArrayList(List<Song> list) {
        mListSong = list;
    }

    @Override
    public int getItemCount() {
        return mListSong.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageMusic, mImageIcoinPlay;
        private TextView mTextTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageMusic = itemView.findViewById(R.id.image_music);
            mImageIcoinPlay = itemView.findViewById(R.id.image_icoin_play);
            mTextTitle = itemView.findViewById(R.id.text_title);
        }

        private void bindData(Song song) {
            String link = song.getArtworkUrl();
            if(link == null) link = song.getAvatarUrl();
            System.out.println(""+song.getArtworkUrl()+" 2  "+song.getAvatarUrl());
            Glide.with(mImageMusic.getContext())
                    .load(link)
                    .error(R.drawable.image_default_music)
                    .into(mImageMusic);
            mTextTitle.setText(song.getTitle());
        }
    }
}
