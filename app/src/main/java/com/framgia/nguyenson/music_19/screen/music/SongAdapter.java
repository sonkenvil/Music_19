package com.framgia.nguyenson.music_19.screen.music;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framgia.nguyenson.music_19.R;
import com.framgia.nguyenson.music_19.data.model.Song;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private List<Song> mSongs;
    private ItemClickListener mListener;

    public SongAdapter(ArrayList<Song> songs) {
        mSongs = songs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_bottom_sheet, parent, false);
        return new ViewHolder(root, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(mSongs.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private TextView mTextTitle, mTextNumber;
        private ItemClickListener mClickListener;

        public ViewHolder(View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            mTextNumber = itemView.findViewById(R.id.text_number);
            mTextTitle = itemView.findViewById(R.id.text_title_bottom);
            mClickListener = itemClickListener;
            itemView.setOnClickListener(this);
        }

        private void bindData(Song song, int position) {
            if (song == null) return;
            mTextTitle.setText(song.getTitle());
            mTextNumber.setText(String.valueOf(position + 1));
        }

        @Override
        public void onClick(View v) {
            mClickListener.onCLickItem(getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onCLickItem(int posittion);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        mListener = itemClickListener;
    }
}
