package com.framgia.nguyenson.music_19.screen.main.genres;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.framgia.nguyenson.music_19.R;
import com.framgia.nguyenson.music_19.data.model.Song;
import com.framgia.nguyenson.music_19.data.repository.SongRepository;
import com.framgia.nguyenson.music_19.data.source.remote.SongRemoteDataSource;
import com.framgia.nguyenson.music_19.screen.music.MusicActivity;
import com.framgia.nguyenson.music_19.service.MusicService;
import com.framgia.nguyenson.music_19.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class GenresFragment extends Fragment implements GenresContract.View,
        GenresAdapter.ItemClickListener {
    private static final String ARGUMENT_GENRE = "ARGUMENT_GENRE";
    private static final int NUMBER_COLUMN = 3;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private GenresPresenter mPresenter;
    private GenresAdapter mGenresAdapter;
    private List<Song> mSongList;
    private String mGenre;

    public static GenresFragment newInstance(String genre) {
        GenresFragment genresFragment = new GenresFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_GENRE, genre);
        genresFragment.setArguments(bundle);
        return genresFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGenre = getArguments().getString(ARGUMENT_GENRE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mRoot = inflater.inflate(R.layout.fragment_genre, container, false);
        return mRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initRecycler();
        initPresenter();
        mPresenter.loadMusic(mGenre, Constants.GenreBase.DEFAULT_LIMIT,
                Constants.GenreBase.DEFAULT_OFFSET);
    }

    public void initRecycler() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), NUMBER_COLUMN);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mGenresAdapter);
    }

    public void initView() {
        mRecyclerView = getView().findViewById(R.id.recycler_main);
        mProgressBar = getView().findViewById(R.id.progress_load);
        mGenresAdapter = new GenresAdapter(new ArrayList<Song>());
        mGenresAdapter.setItemClickListener(this);
    }

    public void initPresenter() {
        SongRemoteDataSource songRemoteDataSource = new SongRemoteDataSource();
        SongRepository songRepository = SongRepository.getInstance(songRemoteDataSource);
        mPresenter = new GenresPresenter(songRepository);
        mPresenter.setView(this);
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showData(List<Song> list) {
        if (list.size() > 0) {
            mSongList = list;
            mGenresAdapter.setSongs(list);
            mGenresAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onClickItem(View view, int position) {
        startActivity(MusicActivity.getInstance(getActivity()));
        getContext().startService(MusicService.getInstance(getActivity(),
                position, mSongList,Constants.MUSIC_ONLINE));
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
