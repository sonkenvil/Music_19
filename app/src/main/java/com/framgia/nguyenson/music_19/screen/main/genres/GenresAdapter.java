package com.framgia.nguyenson.music_19.screen.main.genres;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.framgia.nguyenson.music_19.R;
import com.framgia.nguyenson.music_19.data.model.Song;

import java.util.ArrayList;
import java.util.List;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.ViewHolder> {
    private List<Song> mSongs;
    private ItemClickListener mItemClickListener;

    public GenresAdapter(ArrayList<Song> list) {
        mSongs = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_music, parent, false);
        return new ViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(mSongs.get(position));
    }

    public void setSongs(List<Song> list) {
        mSongs = list;
    }

    @Override
    public int getItemCount() {
        return mSongs.isEmpty() ? 0 : mSongs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private ImageView mImageMusic;
        private TextView mTextTitle;
        private ItemClickListener onClickItem;

        public ViewHolder(View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            mImageMusic = itemView.findViewById(R.id.image_music);
            mTextTitle = itemView.findViewById(R.id.text_title);
            onClickItem = itemClickListener;
            itemView.setOnClickListener(this);
        }

        private void bindData(Song song) {
            if (song == null) return;
            String link = song.getArtworkUrl();
            if (link == null) link = song.getAvatarUrl();
            Glide.with(mImageMusic.getContext())
                    .load(link)
                    .error(R.drawable.image_default_music)
                    .into(mImageMusic);
            mTextTitle.setText(song.getTitle());
        }

        @Override
        public void onClick(View v) {
            onClickItem.onClickItem(v, getAdapterPosition());
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onClickItem(View view, int position);
    }
}
