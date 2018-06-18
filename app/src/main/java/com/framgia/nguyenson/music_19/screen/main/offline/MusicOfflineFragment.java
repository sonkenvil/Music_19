package com.framgia.nguyenson.music_19.screen.main.offline;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.framgia.nguyenson.music_19.R;
import com.framgia.nguyenson.music_19.data.model.Song;
import com.framgia.nguyenson.music_19.data.repository.SongRepository;
import com.framgia.nguyenson.music_19.data.source.local.SongLocalDataSource;
import com.framgia.nguyenson.music_19.screen.music.MusicActivity;
import com.framgia.nguyenson.music_19.service.MusicService;
import com.framgia.nguyenson.music_19.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MusicOfflineFragment extends Fragment implements MusicOfflineAdapter.ItemCLickListener,
        MusicOfflineContract.View {
    private RecyclerView mRecyclerView;
    private MusicOfflineAdapter mAdapter;
    private MusicOfflinePresenter mPresenter;
    private List<Song> mSongList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_music_offline, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        initRecycler();
        initPresenter();
        mPresenter.getAllMusic();
    }

    private void initRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initViews() {
        mRecyclerView = getView().findViewById(R.id.recycler_music_offline);
        mAdapter = new MusicOfflineAdapter(new ArrayList<Song>());
        mAdapter.setCLickListener(this);
    }

    private void initPresenter() {
        SongLocalDataSource localDataSource = new SongLocalDataSource(getContext());
        SongRepository repository = SongRepository.getInstance(localDataSource);
        mPresenter = new MusicOfflinePresenter(repository);
        mPresenter.setView(this);
    }

    @Override
    public void onClickItem(int position) {
        startActivity(MusicActivity.getInstance(getActivity()));
        getContext().startService(MusicService.getInstance(getActivity(),
                position, mSongList, Constants.MUSIC_OFFLINE));
    }

    @Override
    public void showData(List<Song> list) {
        if (list == null) return;
        mSongList = list;
        mAdapter.setSongList(mSongList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
