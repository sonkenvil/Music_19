package com.framgia.nguyenson.music_19.screen.main.genres;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.framgia.nguyenson.music_19.R;
import com.framgia.nguyenson.music_19.data.model.Song;
import com.framgia.nguyenson.music_19.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class GenresFragment extends Fragment implements GenresContract.View {
    static final String KEY = "position";
    static final int NUMBER_COLUMN = 3;
    private RecyclerView mRecyclerView;
    private int mPosition;
    private ProgressBar mProgressBar;
    private GenresPresenter mPresenter;
    private GenresAdapter mGenresAdapter;


    public static GenresFragment newInstance(int position) {
        GenresFragment genresFragment = new GenresFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY, position);
        genresFragment.setArguments(bundle);
        return genresFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(KEY);
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
        mRecyclerView = getView().findViewById(R.id.recycler_main);
        mProgressBar = getView().findViewById(R.id.progress_load);
        mGenresAdapter = new GenresAdapter(new ArrayList<Song>());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), NUMBER_COLUMN);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mGenresAdapter);
        mPresenter = new GenresPresenter();
        mPresenter.setView(this);
        mPresenter.loadMusic(mPosition, Constants.DEFAULT_LIMIT, Constants.DEFAULT_OFFSET);
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
        mGenresAdapter.setArrayList(list);
        mGenresAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
