package com.framgia.nguyenson.music_19.screen.main.offline;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.framgia.nguyenson.music_19.R;
import com.framgia.nguyenson.music_19.data.model.Song;
import com.framgia.nguyenson.music_19.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MusicOfflineAdapter extends RecyclerView.Adapter<MusicOfflineAdapter.ViewHolder> {
    private List<Song> mSongList;
    private ItemCLickListener mCLickListener;

    public MusicOfflineAdapter(ArrayList<Song> list) {
        mSongList = list;
    }

    @NonNull
    @Override
    public MusicOfflineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music_offline,
                parent, false);
        return new ViewHolder(view, mCLickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicOfflineAdapter.ViewHolder holder, int position) {
        holder.bindData(mSongList.get(position));
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextTitle, mTextTime;
        private ImageView mImageMusic;
        private TextView mTextArtist;
        private ItemCLickListener mCLickListener;

        public ViewHolder(View itemView, ItemCLickListener itemClickListener) {
            super(itemView);
            mTextTitle = itemView.findViewById(R.id.text_title_offline);
            mTextTime = itemView.findViewById(R.id.text_time_duration);
            mImageMusic = itemView.findViewById(R.id.image_music_default);
            mTextArtist = itemView.findViewById(R.id.text_artist);
            mCLickListener = itemClickListener;
            itemView.setOnClickListener(this);
        }

        private void bindData(Song song) {
            if (song == null) return;
            mTextTitle.setText(song.getTitle());
            mTextTime.setText(Utils.milliSecondsToTimer(song.getDuration()));
            mTextArtist.setText(song.getArtist());
            loadImageMusic(song.getUri());
        }

        private void loadImageMusic(String uri) {
            android.media.MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(uri);
            byte[] data = retriever.getEmbeddedPicture();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            mImageMusic.setImageBitmap(bitmap);
        }

        @Override
        public void onClick(View v) {
            mCLickListener.onClickItem(getAdapterPosition());
        }
    }

    public void setCLickListener(ItemCLickListener itemCLickListener) {
        mCLickListener = itemCLickListener;
    }

    public void setSongList(List<Song> list) {
        mSongList = list;
    }

    public interface ItemCLickListener {
        void onClickItem(int position);
    }
}
